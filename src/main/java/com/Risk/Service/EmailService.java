package com.Risk.Service;

import com.Risk.DTO.PrenotazioneDTO;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEn;


    @Value("${app.email.destinatario}")
    private String defaultDestinatario;

    public  EmailService(JavaMailSender mailSender, TemplateEngine templateEn){
        this.mailSender = mailSender;
        this.templateEn = templateEn;
    }

    public void sendEmail(String tipologiaScelta, PrenotazioneDTO prenotazioneDTO){
        try {

            MimeMessage message =
                    mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(defaultDestinatario);
            helper.setSubject("Nuova Prenotazione");

            String html = caricaTemplate();

            if(tipologiaScelta != null){
                switch (tipologiaScelta){
                    case "impiantiIdraulici":
                        tipologiaScelta = "Impianti Idraulici";
                        break;
                    case "caldaia" : tipologiaScelta = "Caldaia";
                        break;
                    case "pannelliFotovoltaici": tipologiaScelta = "Pannelli Fotovoltaici";
                        break;
                    case "climatizzazione": tipologiaScelta = "Climatizzatore";
                        break;
                    case "stufe": tipologiaScelta = "Stufe";
                        break;
                    default:
                        html = html.replace("{{tipologiaScelta}}", tipologiaScelta);
                }
                html = html.replace("{{tipologiaScelta}}", tipologiaScelta);

            }
            html = html.replace("{{nome}}", prenotazioneDTO.getNome());
            html = html.replace("{{cognome}}", prenotazioneDTO.getCognome());
            html = html.replace("{{email}}", prenotazioneDTO.getEmail());
            html = html.replace("{{telefono}}", prenotazioneDTO.getTelefono());
            html = html.replace("{{tipologiaPrenotazione}}", prenotazioneDTO.getTipologiaPrenotazione());
            if (prenotazioneDTO.getNote() != null && !prenotazioneDTO.getNote().isBlank()){
                html = html.replace("{{note}}", "Note Cliente: " + prenotazioneDTO.getNote());
            }else {
                html = html.replace("{{note}}", "");
            }

            helper.setText(html, true);
            mailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String caricaTemplate() throws Exception {

        InputStream is =
                getClass().getResourceAsStream(
                        "/templates/Email/Email.html");

        return new String(is.readAllBytes(),
                StandardCharsets.UTF_8);
    }
}
