package com.Risk.Controller;

import com.Risk.DTO.PrenotazioneDTO;
import com.Risk.DTO.TipologiaPrenotazione;
import com.Risk.Service.PrenotazioneService;
import jakarta.validation.Valid;
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

    public PrenotazioneController(PrenotazioneService prenotazioneService){
        this.prenotazioneService = prenotazioneService;

        serviziDescrizioneProperties = new Properties();
        try {
            //Carica il file
            serviziDescrizioneProperties.load(getClass().getResourceAsStream("/DescrizioneServizi.properties"));
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @GetMapping
    public String GetNuovaPrenotazione(@RequestParam("tipo") String tipo, Model model){
        if(tipo != null || !tipo.isEmpty()){
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
    public String SalvaPrenotazione(
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
            boolean successo = prenotazioneService.CreaNuovaPrenotazione(prenotazioneDTO);

            if (successo) {
                // Messaggio di successo tramite Flash Attributes per redirect
                redirectAttributes.addFlashAttribute("successMessage", "Prenotazione Creata Correttamente");

                if (tipo == null) {
                    return "Prenotazione/Prenotazione";
                } else {
                    return "redirect:/nuova/Prenotazione?tipo=" + tipo;
                }
            }
        } catch (Exception e) {
            // Messaggio di errore tramite Flash Attributes per redirect
            redirectAttributes.addFlashAttribute("errorMessage", "Errore durante il tentativo di Prenotazione");

            if (tipo == null) {
                return "Prenotazione/Prenotazione";
            } else {
                return "redirect:/nuova/Prenotazione?tipo=" + tipo;
            }
        }

        // Fallback generale se prenotazioneService non ritorna true ma senza eccezioni
        redirectAttributes.addFlashAttribute("errorMessage", "Errore durante il tentativo di Prenotazione");

        if (tipo == null) {
            return "Prenotazione/Prenotazione";
        } else {
            return "redirect:/nuova/Prenotazione?tipo=" + tipo;
        }
    }
}
