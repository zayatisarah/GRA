package com.example.client.services.imp;

import com.example.client.entites.Portefeuille;
import com.example.client.repositories.PortefeuilleRepository;
import com.example.client.services.PortefeuilleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PortefeuilleServiceImpl implements PortefeuilleService {
    @Autowired
    PortefeuilleRepository portefeuilleRepository;
    @Override
    public Portefeuille addPortefeuille(Portefeuille portefeuille) {
        return portefeuilleRepository.save(portefeuille);
    }

    @Override
    public Portefeuille deletePortefeuille(Long id) {
        portefeuilleRepository.deleteById(id);
        return null;
    }

    @Override
    public List<Portefeuille> getALLPortfeuille() {
        return portefeuilleRepository.findAll();
    }

    @Override
    public Portefeuille updatePorteFeuille(Portefeuille portefeuille) {
        return portefeuilleRepository.save(portefeuille);
    }
}
