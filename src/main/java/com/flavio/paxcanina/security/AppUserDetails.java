package com.flavio.paxcanina.security;

import com.flavio.paxcanina.model.Admin;
import com.flavio.paxcanina.model.Coach;
import com.flavio.paxcanina.model.Owner;
import com.flavio.paxcanina.model.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * AppUserDetails
 * --------------
 * Bridges your domain User to Spring Security.
 * - Authorities are derived from the concrete subclass (Admin/Coach/Owner).
 * - Account enablement reflects RGPD flags: active + not anonymized.
 */
@Getter
public class AppUserDetails implements UserDetails {

    private final User user;

    public AppUserDetails(User user) {
        this.user = user;
    }

    /** Maps the domain role to a single ROLE_* authority. */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + getRole()));
    }

    /** Returns ADMIN / COACH / OWNER / USER (fallback). */
    public String getRole() {
        if (user instanceof Admin)  return "ADMIN";
        if (user instanceof Coach)  return "COACH";
        if (user instanceof Owner)  return "OWNER";
        return "USER";
    }

    /** BCrypt hash already stored in the domain entity. */
    @Override
    public String getPassword() {
        return user.getPasswordHash();
    }

    /** Username is the e-mail address. */
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    // We do not manage "account expiration/lock" at the moment.
    @Override public boolean isAccountNonExpired()     { return true; }
    @Override public boolean isAccountNonLocked()      { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }

    /**
     * Security enablement reflects business flags:
     * - user must be active
     * - user must NOT be anonymized
     */
    @Override
    public boolean isEnabled() {
        return user.isActive() && user.getAnonymizedAt() == null;
    }

    // Convenience getters if needed by services/controllers
    public Admin getAdminOrThrow() {
        if (user instanceof Admin a) return a;
        throw new IllegalStateException("Current user is not an Admin");
    }

    public Coach getCoachOrNull() {
        return (user instanceof Coach c) ? c : null;
    }

    public Owner getOwnerOrNull() {
        return (user instanceof Owner o) ? o : null;
    }

    public Integer getIdUser() {
        return user.getIdUser();
    }
}
