package com.example.client.services;

import com.example.client.entites.Cotation;

import java.util.List;

public interface CotationService {
    Cotation addCotation(Cotation cotation);
    List<Cotation> getAllCotations();
    Cotation getCotationById(Long id);
    void deleteCotation(Long id);
}
