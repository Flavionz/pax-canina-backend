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

@Getter
public class AppUserDetails implements UserDetails {

    private final User user;

    public AppUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + getRole()));
    }

    public String getRole() {
        if (user instanceof Admin)  return "ADMIN";
        if (user instanceof Coach)  return "COACH";
        if (user instanceof Owner)  return "OWNER";
        return "USER";
    }

    public Admin getAdmin() {
        if (user instanceof Admin) return (Admin) user;
        throw new IllegalStateException("Current user is not an Admin");
    }

    @Override
    public String getPassword() {
        return user.getPasswordHash(); // <-- usa il campo giusto!
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}
