package com.Risk.Security;

import com.Risk.Entity.Utente;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class DatabaseUserDetails implements UserDetails {

    private final int idUtente;
    private final String username;
    private final String password;

    public DatabaseUserDetails(Utente utente) {
        this.idUtente = utente.getIdUtente();
        this.username = utente.getUsername();
        this.password = utente.getPassword();
    }

    public int getIdUtente() {
        return idUtente;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Non abbiamo ruoli, quindi restituiamo lista vuota
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // account sempre valido
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // account mai bloccato
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // credenziali sempre valide
    }

    @Override
    public boolean isEnabled() {
        return true; // account sempre abilitato
    }
}
