package com.Risk.Security;

import com.Risk.Entity.Utente;
import com.Risk.Repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public class DatabaseUserDetailsService implements UserDetailsService {
    @Autowired
    private UtenteRepository utenteRepository;

    @Override
    public UserDetails loadUserByUsername(String username){
        Optional<Utente> optUtente = utenteRepository.findByUsername(username);

        if (optUtente.isPresent()){
            return new DatabaseUserDetails(optUtente.get());
        }else {
            throw new UsernameNotFoundException("Utente non trovato");
        }
    }
}
