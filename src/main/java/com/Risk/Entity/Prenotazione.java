package com.Risk.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Entity
public class Prenotazione {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idPrenotazione;

    @NotBlank
    private String TipologiaPrenotazione;

    @NotNull
    private LocalDate DataAppuntamento;

    @Size(max = 255, message = "Nota Inserita troppo lunga")
    private String Note;

    private boolean isMailInviata;

    @ManyToOne
    @JoinColumn(name = "id_Cliente")
    private Cliente cliente;

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public int getIdPrenotazione() {
        return idPrenotazione;
    }

    public void setIdPrenotazione(int idPrenotazione) {
        this.idPrenotazione = idPrenotazione;
    }

    public String getTipologiaPrenotazione() {
        return TipologiaPrenotazione;
    }

    public void setTipologiaPrenotazione(String tipologiaPrenotazione) {
        TipologiaPrenotazione = tipologiaPrenotazione;
    }

    public LocalDate getDataAppuntamento() {
        return DataAppuntamento;
    }

    public void setDataAppuntamento(LocalDate dataAppuntamento) {
        DataAppuntamento = dataAppuntamento;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }
    
    public boolean isMailInviata() {
        return isMailInviata;
    }

    public void setMailInviata(boolean mailInviata) {
        isMailInviata = mailInviata;
    }
}
