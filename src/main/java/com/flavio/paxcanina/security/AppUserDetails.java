package com.flavio.paxcanina.security;

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
        // Adatta il metodo per ottenere il ruolo dal tuo modello Utilisateur
        return List.of(new SimpleGrantedAuthority("ROLE_" + utilisateur.getRole()));
        // Sostituisci "getRole()" con il nome del metodo/campo che usi per il ruolo
    }

    @Override
    public String getPassword() {
        return utilisateur.getPassword();
    }

    @Override
    public String getUsername() {
        return utilisateur.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // O gestisci logica se hai scadenza account
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // O gestisci logica se hai blocco account
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // O gestisci logica se hai scadenza credenziali
    }

    @Override
    public boolean isEnabled() {
        // Se hai un campo per la verifica email, puoi usarlo come nel codice del prof
        // return utilisateur.getJetonVerificationEmail() == null;
        return true; // O la logica che preferisci
    }
}
