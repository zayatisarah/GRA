package com.example.client.controllers;

import com.example.client.entites.Cotation;
import com.example.client.services.CotationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cotations")
@CrossOrigin("*")
public class CotationController {

    private final CotationService cotationService;

    @Autowired
    public CotationController(CotationService cotationService) {
        this.cotationService = cotationService;
    }

    @PostMapping
    public Cotation addCotation(@RequestBody Cotation cotation) {
        return cotationService.addCotation(cotation);
    }

    @GetMapping
    public List<Cotation> getAllCotations() {
        return cotationService.getAllCotations();
    }

    @GetMapping("/{id}")
    public Cotation getCotationById(@PathVariable Long id) {
        return cotationService.getCotationById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteCotation(@PathVariable Long id) {
        cotationService.deleteCotation(id);
    }
}
