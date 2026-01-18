package com.Risk.Controller;

import com.Risk.Entity.Immagine;
import com.Risk.Repository.ImmagineRepository;
import com.Risk.Service.IndexService;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalModelAttributes {

    private final IndexService indexService;
    private final ImmagineRepository immagineRepository;

    public GlobalModelAttributes(IndexService indexService, ImmagineRepository immagineRepository) {
        this.indexService = indexService;
        this.immagineRepository = immagineRepository;
    }

    @ModelAttribute("img")
    public Map<String, String> populateImages() throws IOException {
        List<Immagine> img = immagineRepository.findAll();
        Map<String, String> imgMap = new HashMap<>();

        //Salvo l'array di byte se presente
        for (Immagine i : img) {
            if (i.getPathFoto() == null && i.getPathImmagineDirette() == null) {
                indexService.recuperaImmagine(i.getIdImmagine());
            }
        }

        //Salvo in base64 se presente
        for (Immagine i : img) {
            if (i.getPathImmagineDirette() != null) {
                String base64Img = indexService.convertiInBase64(i.getPathImmagineDirette());
                imgMap.put("img_logo", "data:image/jpeg;base64," + base64Img);
            }
        }

        if(img.size() > 1) imgMap.put("caldaia", img.get(1).getPathFoto());
        if(img.size() > 2) imgMap.put("img_ImpiantiIdraulici", img.get(2).getPathFoto());
        if(img.size() > 3) imgMap.put("img_PannelliFotovoltaici", img.get(3).getPathFoto());
        if(img.size() > 4) imgMap.put("img_Climatizzatore", img.get(4).getPathFoto());
        if(img.size() > 5) imgMap.put("img_StufaPellet", img.get(5).getPathFoto());

        return imgMap;
    }
}
