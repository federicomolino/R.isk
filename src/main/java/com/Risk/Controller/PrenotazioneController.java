package com.Risk.Controller;

import com.Risk.DTO.PrenotazioneDTO;
import com.Risk.DTO.TipologiaPrenotazione;
import com.Risk.Service.EmailService;
import com.Risk.Service.PrenotazioneService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Properties;

@Controller
@RequestMapping("/nuova/Prenotazione")
public class PrenotazioneController {

    private PrenotazioneService prenotazioneService;
    private Properties serviziDescrizioneProperties;
    private EmailService emailService;

    @Autowired
    public PrenotazioneController(PrenotazioneService prenotazioneService, EmailService emailService){
        this.prenotazioneService = prenotazioneService;
        this.emailService = emailService;

        serviziDescrizioneProperties = new Properties();
        try {
            //Carica il file
            serviziDescrizioneProperties.load(getClass().getResourceAsStream("/DescrizioneServizi.properties"));
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @GetMapping
    public String getNuovaPrenotazione(@RequestParam("tipo") String tipo, Model model){

        if(tipo != null && !tipo.isEmpty()){
            String descrizione = serviziDescrizioneProperties.getProperty(tipo + ".descrizione");
            String titolo = serviziDescrizioneProperties.getProperty(tipo + ".titolo");
            model.addAttribute("titolo",titolo);
            model.addAttribute("descrizione",descrizione);
        }
        model.addAttribute("tipologiaPrenotazione", TipologiaPrenotazione.values());
        PrenotazioneDTO dto = new PrenotazioneDTO();
        dto.setTipo(tipo);
        model.addAttribute("formNuovaPrenotazione", dto);
        return "Prenotazione/Prenotazione";
    }

    @PostMapping
    public String salvaPrenotazione(
            @Valid @ModelAttribute("formNuovaPrenotazione") PrenotazioneDTO prenotazioneDTO,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes,
            @RequestParam(value = "Tipo", required = false) String tipo) {

        // Aggiungo sempre le tipologie al model (serve se si ritorna alla view senza redirect)
        model.addAttribute("tipologiaPrenotazione", TipologiaPrenotazione.values());

        // Se ci sono errori di validazione, rimango nella stessa view
        if (bindingResult.hasErrors()) {
            return "Prenotazione/Prenotazione";
        }

        try {
            prenotazioneService.creaNuovaPrenotazione(prenotazioneDTO, tipo);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Prenotazione effettuata, verrete ricontattati al più presto");
        } catch (MailException ex) {
            // Se c'è un errore nell'invio mail
            redirectAttributes.addFlashAttribute("successMessage",
                    "Prenotazione effettuata, verrete ricontattati al più presto");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Errore durante il tentativo di prenotazione");
            return tipo == null ? "Prenotazione/Prenotazione" : "redirect:/nuova/Prenotazione?tipo=" + tipo;
        }

        // Se siamo arrivati qui senza eccezioni bloccanti
        return tipo == null ? "Prenotazione/Prenotazione" : "redirect:/nuova/Prenotazione?tipo=" + tipo;
    }
}
