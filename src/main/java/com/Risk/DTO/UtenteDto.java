package com.Risk.DTO;

import jakarta.validation.constraints.NotBlank;

public class UtenteDto {

    @NotBlank(message = "Username obbligatorio")
    private String Username;
    @NotBlank(message = "Password obbligatoria")
    private String Password;

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
