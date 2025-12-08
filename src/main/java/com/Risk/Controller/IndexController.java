package com.Risk.Controller;

import com.Risk.Entity.Immagine;
import com.Risk.Repository.ImmagineRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
public class IndexController {
    private ImmagineRepository immagineRepository;

    public IndexController(ImmagineRepository immagineRepository){
        this.immagineRepository = immagineRepository;
    }

    @GetMapping
    public String indexPage(Model model){
        List<Immagine> img = immagineRepository.findAll();
        Map<String, String> imgMap = new HashMap<>();
        imgMap.put("caldaia", img.get(1).getPathFoto());
        imgMap.put("img_ImpiantiIdraulici", img.get(2).getPathFoto());
        imgMap.put("img_PannelliFotovoltaici", img.get(3).getPathFoto());
        imgMap.put("img_Climatizzatore", img.get(4).getPathFoto());
        imgMap.put("img_StufaPellet", img.get(5).getPathFoto());
        model.addAttribute("img",imgMap);
        return "Index";
    }
}
