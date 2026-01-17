package com.Risk.Controller;

import com.Risk.Repository.ImmagineRepository;
import com.Risk.Service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class IndexController {
    private ImmagineRepository immagineRepository;
    private IndexService indexService;

    @Autowired
    public IndexController(ImmagineRepository immagineRepository, IndexService indexService) {
        this.immagineRepository = immagineRepository;
        this.indexService = indexService;
    }

    @GetMapping("/")
    public String indexPage() {
        // nessuna logica img, tutta nel GlobalModelAttributes
        return "Index";
    }
}
