package com.Risk.Controller;

import com.Risk.DTO.PrenotazioneDTO;
import com.Risk.DTO.UtenteDto;
import com.Risk.Repository.PrenotazioneRepository;
import com.Risk.Repository.UtenteRepository;
import com.Risk.Service.PasswordUtil;
import com.Risk.Service.PrenotazioneService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/Accedi")
public class AccediController {

    private UtenteRepository utenteRepository;
    private PasswordUtil passwordUtil;
    private PrenotazioneRepository prenotazioneRepository;
    private PrenotazioneService prenotazioneService;

    @Autowired
    public AccediController(UtenteRepository utenteRepository, PasswordUtil passwordUtil,
                            PrenotazioneRepository prenotazioneRepository, PrenotazioneService prenotazioneService){
        this.utenteRepository = utenteRepository;
        this.passwordUtil = passwordUtil;
        this.prenotazioneRepository = prenotazioneRepository;
        this.prenotazioneService = prenotazioneService;
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

    @GetMapping("/Private/Appuntamento/{idPrenotazione}")
    public String getDettaglioAppuntamento(@PathVariable("idPrenotazione") int idPrenotazione, Model model,
                                           RedirectAttributes redirectAttributes){
        try {
            model.addAttribute("formDettaglioAppuntamento",
                    prenotazioneService.RecuperaAppuntamento(idPrenotazione));
            return "Private/DettaglioAppuntamento";
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("errorMessage", "Errore durante " +
                    "il recupero dell'Appuntamento");
            return "redirect:Accedi/Private/VerificaAppuntamenti";
        }
    }

    @PostMapping("/Private/Appuntamento")
    public String appuntamentoLetto(@ModelAttribute("formDettaglioAppuntamento") PrenotazioneDTO prenotazioneDTO,
                                    RedirectAttributes redirectAttributes){
        if (prenotazioneDTO.getIdPrenotazione() == 0){
            redirectAttributes.addFlashAttribute("errorMessage", "Errore durante la lettura della" +
                    "mail");
            return "redirect:/Accedi/Private/Appuntamento/" + prenotazioneDTO.getIdPrenotazione();
        }
        try {
            prenotazioneService.letturaAppuntamento(prenotazioneDTO.getIdPrenotazione());
            redirectAttributes.addFlashAttribute("successMessage",
                    "Appuntamento segnato come letto");
            return "redirect:/Accedi/Private/VerificaAppuntamenti";
        }catch (Exception ex){
            redirectAttributes.addFlashAttribute("errorMessage", "Errore durante la lettura della" +
                    "mail");
            return "redirect:/Accedi/Private/Appuntamento/" + prenotazioneDTO.getIdPrenotazione();
        }
    }
}
