package com.Risk.DTO;

import jakarta.validation.constraints.*;

public class PrenotazioneDTO {

    @NotBlank(message = "Il campo non può essere vuoto")
    private String Nome;

    @NotBlank(message = "Il campo non può essere vuoto")
    private String Cognome;

    @NotBlank(message = "Il campo non può essere vuoto")
    private String Email;

    @NotNull(message = "Il campo non può essere vuoto")
    @Pattern(regexp = "^\\+?[0-9]{8,15}$", message = "Il numero non è valido")
    private String Telefono;

    @NotBlank(message = "Selezionare un valore")
    private String TipologiaPrenotazione;

    @Size(max = 255, message = "Il valore massimo consentito è di 255 caratteri")
    private String Note;

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }

    public String getCognome() {
        return Cognome;
    }

    public void setCognome(String cognome) {
        Cognome = cognome;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String telefono) {
        Telefono = telefono;
    }

    public String getTipologiaPrenotazione() {
        return TipologiaPrenotazione;
    }

    public void setTipologiaPrenotazione(String tipologiaPrenotazione) {
        TipologiaPrenotazione = tipologiaPrenotazione;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }
}