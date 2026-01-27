package com.Risk.Controller;

import com.Risk.DTO.UtenteDto;
import com.Risk.Repository.PrenotazioneRepository;
import com.Risk.Repository.UtenteRepository;
import com.Risk.Service.PasswordUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequestMapping("/Accedi")
public class AccediController {

    private UtenteRepository utenteRepository;
    private PasswordUtil passwordUtil;
    private PrenotazioneRepository prenotazioneRepository;

    @Autowired
    public AccediController(UtenteRepository utenteRepository, PasswordUtil passwordUtil,
                            PrenotazioneRepository prenotazioneRepository){
        this.utenteRepository = utenteRepository;
        this.passwordUtil = passwordUtil;
        this.prenotazioneRepository = prenotazioneRepository;
    }

    @GetMapping()
    public String accedi(Model model, HttpSession session,
                         @RequestParam(value = "logout", required = false) String logout) {

        // Messaggio login fallito
        String errorMessage = (String) session.getAttribute("errorMessage");
        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
            session.removeAttribute("errorMessage");
        }

        model.addAttribute("formAccesso", new UtenteDto());
        return "Accedi/Accedi";
    }

    @GetMapping("/Private/VerificaAppuntamenti")
    public String getVerificaAppuntamenti(Model model, Principal principal){
        if (principal == null){
            return "redirect:/";
        }
        model.addAttribute("prenotazioni", prenotazioneRepository.findAllOrderByDataAppuntamento());
        return "Private/VerificaAppuntamenti";
    }
}
