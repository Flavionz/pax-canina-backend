package com.flavio.paxcanina.service;

import com.flavio.paxcanina.security.AppUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

@Service
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String getRole(AppUserDetails userDetails) {
        return userDetails.getAuthorities().stream()
                .map(r -> r.getAuthority())
                .findFirst()
                .orElse(null);
    }

    @Override
    public String generateToken(AppUserDetails userDetails, String role) {
        long expirationMillis = 1000 * 60 * 60 * 24; // 24 ore, puoi modificarlo a piacere
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationMillis);

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claims(Map.of("role", getRole(userDetails)))
                .expiration(expiryDate)
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();
    }


    @Override
    public String getSubjectFromJwt(String jwt) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(jwt)
                .getPayload()
                .getSubject();
    }

    @Override
    public boolean isTokenValid(String jwt, UserDetails userDetails) {
        try {
            final String username = getSubjectFromJwt(jwt);
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(jwt));
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isTokenExpired(String jwt) {
        Date expiration = getClaims(jwt).getExpiration();
        return expiration.before(new Date());
    }

    private Claims getClaims(String jwt) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
    }
}
