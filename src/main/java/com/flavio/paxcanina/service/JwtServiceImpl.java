package com.flavio.paxcanina.service;

import com.flavio.paxcanina.security.AppUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Service
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.secret}")
    private String jwtSecretBase64;

    /** Access token lifetime in seconds (default 24h). */
    @Value("${jwt.access.expiration-seconds:86400}")
    private long accessExpSeconds;

    /** Issuer & Audience for stronger validation. */
    @Value("${jwt.issuer:pax-canina}")
    private String issuer;

    @Value("${jwt.audience:web}")
    private String audience;

    /** Allowed clock skew tolerance for parser. */
    @Value("${jwt.allowed-skew-seconds:60}")
    private long allowedSkewSeconds;

    /* =================
       Public API
       ================= */

    @Override
    public String getRole(AppUserDetails userDetails) {
        return userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse(null);
    }

    @Override
    public String generateToken(AppUserDetails userDetails, String role) {
        final String effectiveRole = (role != null ? role : getRole(userDetails));

        final Instant now = Instant.now();
        final Instant nbf = now.minusSeconds(5);
        final Instant exp = now.plusSeconds(accessExpSeconds);

        return Jwts.builder()
                .header()
                .type("JWT")
                .and()
                .subject(userDetails.getUsername())
                .issuer(issuer)
                .audience().add(audience).and()
                .issuedAt(Date.from(now))
                .notBefore(Date.from(nbf))
                .expiration(Date.from(exp))
                .id(UUID.randomUUID().toString())
                .claims(Map.of("role", effectiveRole))
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();
    }

    @Override
    public String getSubjectFromJwt(String jwt) {
        return parseClaims(jwt).getSubject();
    }

    @Override
    public boolean isTokenValid(String jwt, UserDetails userDetails) {
        try {
            Claims c = parseClaims(jwt);

            String username = c.getSubject();
            if (username == null || !username.equals(userDetails.getUsername())) return false;
            Date exp = c.getExpiration();
            if (exp == null || exp.before(new Date())) return false;
            if (!issuer.equals(c.getIssuer())) return false;
            Object aud = c.get("aud");
            if (aud instanceof String) {
                if (!audience.equals(aud)) return false;
            } else if (aud instanceof Iterable<?> iterable) {
                boolean ok = false;
                for (Object v : iterable) if (audience.equals(v)) { ok = true; break; }
                if (!ok) return false;
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /* =================
       Private helpers
       ================= */

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecretBase64);
        return Keys.hmacShaKeyFor(keyBytes); // HS256/384/512 compatible
    }

    private Claims parseClaims(String jwt) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .clockSkewSeconds(allowedSkewSeconds)
                .requireIssuer(issuer)
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
    }

}
