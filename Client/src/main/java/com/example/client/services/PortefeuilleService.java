package com.example.client.services;

import com.example.client.entites.Portefeuille;

import java.util.List;

public interface PortefeuilleService {
    Portefeuille addPortefeuille(Portefeuille portefeuille);
    Portefeuille deletePortefeuille(Long id);
    List<Portefeuille> getALLPortfeuille();
    Portefeuille updatePorteFeuille (Portefeuille portefeuille);
}

