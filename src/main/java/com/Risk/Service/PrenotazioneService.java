package com.Risk.Service;

import com.Risk.DTO.PrenotazioneDTO;
import com.Risk.Entity.Cliente;
import com.Risk.Entity.Prenotazione;
import com.Risk.Repository.ClienteRepository;
import com.Risk.Repository.PrenotazioneRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class PrenotazioneService {

    private PrenotazioneRepository prenotazioneRepository;
    private EmailService emailService;

    private ClienteRepository clienteRepository;

    @Autowired
    public PrenotazioneService(PrenotazioneRepository prenotazioneRepository, ClienteRepository clienteRepository,
                               EmailService emailService){
        this.prenotazioneRepository = prenotazioneRepository;
        this.clienteRepository = clienteRepository;
        this.emailService = emailService;
    }

    @Transactional
    public boolean creaNuovaPrenotazione(PrenotazioneDTO prenotazioneDTO, String tipologiaPrenotazione){
        if (prenotazioneDTO == null){
            throw new IllegalArgumentException();
        }
        boolean isPrenotato = false;
        try {
            Cliente cliente = new Cliente();
            cliente.setNome(prenotazioneDTO.getNome());
            cliente.setCognome(prenotazioneDTO.getCognome());
            cliente.setEmail(prenotazioneDTO.getEmail());
            cliente.setTelefono(Long.parseLong(prenotazioneDTO.getTelefono()));
            clienteRepository.save(cliente);

            Prenotazione prenotazione = new Prenotazione();
            prenotazione.setTipologiaPrenotazione(prenotazioneDTO.getTipologiaPrenotazione());
            prenotazione.setDataAppuntamento(LocalDate.now());
            prenotazione.setNote(prenotazioneDTO.getNote());
            prenotazione.setCliente(cliente);
            prenotazioneRepository.save(prenotazione);

            //Tento l'invio della mail
            try {
                emailService.sendEmail(tipologiaPrenotazione, prenotazioneDTO);
                prenotazione.setMailInviata(true);
            }catch (MailException ex){
                prenotazione.setMailInviata(false);
                throw new Exception("Errore durante il tentativo di invio della mail");
            }
            return isPrenotato = true;
        }catch (Exception e){
            return isPrenotato;
        }
    }

    public PrenotazioneDTO RecuperaAppuntamento(int idPrenotazione){
        Optional<Prenotazione> p = prenotazioneRepository.findById(idPrenotazione);
        if (p.isEmpty()){
            return null;
        }
        Cliente c = p.get().getCliente();
        if(c == null){
            return null;
        }

        PrenotazioneDTO prenotazioneDTO = new PrenotazioneDTO();
        prenotazioneDTO.setIdPrenotazione(idPrenotazione);
        prenotazioneDTO.setNome(c.getNome().toUpperCase());
        prenotazioneDTO.setCognome(c.getCognome().toUpperCase());
        prenotazioneDTO.setEmail(c.getEmail());
        prenotazioneDTO.setTelefono(String.valueOf(c.getTelefono()));
        prenotazioneDTO.setTipologiaPrenotazione(p.get().getTipologiaPrenotazione());
        prenotazioneDTO.setNote(p.get().getNote());
        prenotazioneDTO.setMailInviata(p.get().isMailInviata());
        prenotazioneDTO.setDataRichiesta(p.get().getDataAppuntamento());
        return prenotazioneDTO;
    }

    public void letturaAppuntamento(int idPrenotazione){
        Optional<Prenotazione> p = prenotazioneRepository.findById(idPrenotazione);
        if (p.isPresent()){
            Prenotazione prenotazione = p.get();
            prenotazione.setMailInviata(true);
            prenotazioneRepository.save(prenotazione);
        }else {
            throw new RuntimeException("Prenotazione con id: " + idPrenotazione + " non trovata");
        }
    }
}
