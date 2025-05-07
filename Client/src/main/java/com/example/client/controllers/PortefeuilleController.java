package com.example.client.controllers;

import com.example.client.entites.Portefeuille;
import com.example.client.repositories.PortefeuilleRepository;
import com.example.client.services.PortefeuilleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/portefeuille")
public class PortefeuilleController {
    @Autowired
    private final PortefeuilleService portefeuilleService; // Ajout de final pour garantir l'injection
    @Autowired
    private final PortefeuilleRepository portefeuilleRepository;

    // ✅ Tester la connexion à la base de données
    @GetMapping("/ping")
    public String testDatabaseConnection() {
        return "Connexion à la base de données réussie pour le portefeuille !";
    }

    // ✅ Ajouter un portefeuille
    @PostMapping("/add")
    public Portefeuille addPortefeuille(@RequestBody Portefeuille portefeuille) {
        return portefeuilleRepository.save(portefeuille);  // Assurez-vous que le repository fonctionne
    }

    // ✅ Récupérer tous les portefeuilles
    @GetMapping("/all")
    public List<Portefeuille> getAllPortefeuilles() {
        return portefeuilleRepository.findAll();
    }

    // ✅ Récupérer un portefeuille par ID
    @GetMapping("/{id}")
    public Optional<Portefeuille> getPortefeuilleById(@PathVariable Long id) {
        return portefeuilleRepository.findById(id);
    }

    // ✅ Supprimer un portefeuille
    @DeleteMapping("/{id}")
    public String deletePortefeuille(@PathVariable Long id) {
        portefeuilleRepository.deleteById(id);
        return "Portefeuille supprimé avec succès !";
    }

    // ✅ Mettre à jour un portefeuille
    @PutMapping("/updatePortefeuille/{id}")
    public Portefeuille updatePortefeuille(@RequestBody Portefeuille portefeuille, @PathVariable Long id) {
        return portefeuilleService.updatePorteFeuille(portefeuille);
    }
}

