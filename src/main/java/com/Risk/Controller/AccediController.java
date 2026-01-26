package com.Risk.Controller;

import com.Risk.DTO.UtenteDto;
import com.Risk.Repository.PrenotazioneRepository;
import com.Risk.Repository.UtenteRepository;
import com.Risk.Service.PasswordUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

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

    @GetMapping
    public String accedi(Model model){
        UtenteDto ute = new UtenteDto();
        model.addAttribute("formAccesso", ute);
        return "Accedi/Accedi";
    }

    @PostMapping
    public String accedi(@Valid @ModelAttribute("formAccesso") UtenteDto utenteDto,
                         BindingResult bindingResult, RedirectAttributes redirectAttributes, HttpServletRequest request){

        if (bindingResult.hasErrors()){
            return "redirect:/Accedi";
        }

        try {
            if(passwordUtil.verificaPassword(utenteDto.getUsername(), utenteDto.getPassword())){
                List<GrantedAuthority> authorities = List.of(
                        new SimpleGrantedAuthority("ROLE_USER") // ruolo tecnico, anche se non lo usi
                );
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                utenteDto.getUsername(),
                                null,
                                authorities
                        );
                //Creiamo un nuovo contesto
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                context.setAuthentication(authToken);
                SecurityContextHolder.setContext(context);

                //Salviamo la sessione nel repository ufficiale
                HttpSessionSecurityContextRepository repo = new HttpSessionSecurityContextRepository();
                repo.saveContext(context, request, null);
                return "redirect:/";
            }else {
                redirectAttributes.addFlashAttribute("errorMessage",
                        "Credenziali non valide");
            }
        }catch (Exception ex){
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Errore generico durante il tentativo di verifica dell'utente");
        }
        return "redirect:/Accedi";
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
