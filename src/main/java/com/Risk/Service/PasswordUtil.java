package com.Risk.Service;

import com.Risk.Entity.Utente;
import com.Risk.Repository.UtenteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PasswordUtil {

    private static final Logger log = LoggerFactory.getLogger(PasswordUtil.class);
    private final PasswordEncoder encoder = new BCryptPasswordEncoder();
    private UtenteRepository utenteRepository;

    @Autowired
    public PasswordUtil(UtenteRepository utenteRepository){
        this.utenteRepository = utenteRepository;
    }

    public boolean verificaPassword(String username, String password){
        //creaModificaPassword();
        if (password.isBlank() || username.isBlank()){
            return false;
        }

        boolean passwordCorretta = false;

        Optional<Utente> user = utenteRepository.findByUsername(username);
        if(user.isPresent()){
            passwordCorretta = verifyPassword(password, user.get().getPassword());
            return passwordCorretta;
        }
        return passwordCorretta;
    }

    public void creaModificaPassword(){
        String nuovaPassword = "Federico004@";
        System.out.println(hash(nuovaPassword));
    }

    //Solo per creazione dell'hash per la password
    private String hash (String password){
        return encoder.encode(password);
    }

    private boolean verifyPassword(String password, String encodedPaasword){
        return encoder.matches(password,encodedPaasword);
    }
}
