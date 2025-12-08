package com.Risk.Service;

import com.Risk.DTO.PrenotazioneDTO;
import com.Risk.Entity.Cliente;
import com.Risk.Entity.Prenotazione;
import com.Risk.Repository.ClienteRepository;
import com.Risk.Repository.PrenotazioneRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class PrenotazioneService {

    private PrenotazioneRepository prenotazioneRepository;

    private ClienteRepository clienteRepository;

    @Autowired
    public PrenotazioneService(PrenotazioneRepository prenotazioneRepository, ClienteRepository clienteRepository){
        this.prenotazioneRepository = prenotazioneRepository;
        this.clienteRepository = clienteRepository;
    }

    @Transactional
    public boolean CreaNuovaPrenotazione(PrenotazioneDTO prenotazioneDTO){
        if (prenotazioneDTO == null){
            throw new IllegalArgumentException();
        }
        boolean isPrenotato = false;
        try {
            Cliente cliente = new Cliente();
            cliente.setNome(prenotazioneDTO.getNome());
            cliente.setCognome(prenotazioneDTO.getCognome());
            cliente.setEmail(prenotazioneDTO.getEmail());
            cliente.setTelefono(prenotazioneDTO.getTelefono());
            clienteRepository.save(cliente);

            Prenotazione prenotazione = new Prenotazione();
            prenotazione.setTipologiaPrenotazione(prenotazioneDTO.getTipologiaPrenotazione());
            prenotazione.setDataAppuntamento(LocalDate.now());
            prenotazione.setNote(prenotazioneDTO.getNote());
            prenotazione.setCliente(cliente);
            prenotazioneRepository.save(prenotazione);
            return isPrenotato = true;
        }catch (Exception e){
            return isPrenotato;
        }
    }
}
