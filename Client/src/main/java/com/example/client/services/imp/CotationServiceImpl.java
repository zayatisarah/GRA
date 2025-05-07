package com.example.client.services.imp;

import com.example.client.entites.Cotation;
import com.example.client.repositories.CotationRepository;
import com.example.client.services.CotationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CotationServiceImpl implements CotationService {

    private final CotationRepository cotationRepository;
    @Autowired
    public CotationServiceImpl(CotationRepository cotationRepository) {
        this.cotationRepository = cotationRepository;
    }

    @Override
    public Cotation addCotation(Cotation cotation) {
        return cotationRepository.save(cotation);
    }

    @Override
    public List<Cotation> getAllCotations() {
        return cotationRepository.findAll();
    }

    @Override
    public Cotation getCotationById(Long id) {
        Optional<Cotation> cotation = cotationRepository.findById(id);
        return cotation.orElse(null);
    }

    @Override
    public void deleteCotation(Long id) {
        cotationRepository.deleteById(id);

    }
}
