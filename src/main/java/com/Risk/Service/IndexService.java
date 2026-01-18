package com.Risk.Service;

import com.Risk.Entity.Immagine;
import com.Risk.Repository.ImmagineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

@Service
public class IndexService {

    private ImmagineRepository immagineRepository;

    @Autowired
    public IndexService(ImmagineRepository immagineRepository){
        this.immagineRepository = immagineRepository;
    }

    public void recuperaImmagine(int idImmagine) throws IOException {

        Immagine rImg = immagineRepository.findById(idImmagine).get();
        String immagine = rImg.getCodiceFoto();


        switch (immagine.substring(4)){
            case "logo":
                byte[] b = Files.readAllBytes(
                        Paths.get("Immagini/Logo.png")
                );
                rImg.setPathImmagineDirette(b);
                immagineRepository.save(rImg);
                break;
        }
    }

    public String convertiInBase64(byte [] img){
        String base64Img = Base64.getEncoder().encodeToString(img);
        return base64Img;
    }

}
