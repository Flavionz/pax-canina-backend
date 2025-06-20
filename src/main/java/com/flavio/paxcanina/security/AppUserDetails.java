package com.flavio.paxcanina.security;

import com.flavio.paxcanina.model.Admin;
import com.flavio.paxcanina.model.Coach;
import com.flavio.paxcanina.model.Proprietaire;
import com.flavio.paxcanina.model.Utilisateur;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
public class AppUserDetails implements UserDetails {

    private final Utilisateur utilisateur;

    public AppUserDetails(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + getRole()));
    }

    /**
     * Ritorna la stringa del ruolo ("ADMIN", "COACH", "PROPRIETAIRE" o "UTILISATEUR").
     */
    public String getRole() {
        if (utilisateur instanceof Admin)         return "ADMIN";
        if (utilisateur instanceof Coach)         return "COACH";
        if (utilisateur instanceof Proprietaire)  return "PROPRIETAIRE";
        return "UTILISATEUR";
    }

    /**
     * Ritorna l'oggetto Admin per l'utente loggato, o lancia un'eccezione se non è un Admin.
     * Utilizzato da AdminService.getCurrentAdmin(...)
     */
    public Admin getAdmin() {
        if (utilisateur instanceof Admin) {
            return (Admin) utilisateur;
        }
        throw new IllegalStateException("L'utente corrente non è un Admin");
    }

    @Override
    public String getPassword() {
        return utilisateur.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return utilisateur.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
