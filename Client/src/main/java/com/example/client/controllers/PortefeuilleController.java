package com.example.client.controllers;

import com.example.client.entites.Action;
import com.example.client.entites.Actionnaire;
import com.example.client.entites.DTO.PortefeuilleDTO;
import com.example.client.entites.Portefeuille;
import com.example.client.repositories.ActionRepository;
import com.example.client.repositories.ActionnaireRepository;
import com.example.client.repositories.PortefeuilleRepository;
import com.example.client.services.PortefeuilleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
 @Autowired
 private final ActionnaireRepository actionnaireRepository;
 @Autowired
 private final ActionRepository actionRepository;
    // ✅ Tester la connexion à la base de données
    @GetMapping("/ping")
    public String testDatabaseConnection() {
        return "Connexion à la base de données réussie pour le portefeuille !";
    }

    // ✅ Ajouter un portefeuille
    @PostMapping("/add")
    public ResponseEntity<?> create(@RequestBody PortefeuilleDTO dto) {
        if (dto.getIdActionnaire() == null || dto.getIdAction() == null) {
            return ResponseEntity.badRequest().body("ID d'actionnaire ou d'action manquant !");
        }

        Actionnaire a = actionnaireRepository.findById(dto.getIdActionnaire())
                .orElseThrow(() -> new IllegalArgumentException("Actionnaire introuvable"));
        Action action = actionRepository.findById(dto.getIdAction())
                .orElseThrow(() -> new IllegalArgumentException("Action introuvable"));

        Portefeuille p = new Portefeuille();
        p.setActionnaire(a);
        p.setAction(action);
        p.setQuantite(dto.getQuantite());
        p.setValeurTotale(action.getPrix() * dto.getQuantite());
        p.setDateCreation(LocalDateTime.now());

        // ⚠️ Ici le problème : si `setUserCreation(...)` est manquant ou null
        p.setUserCreation("admin"); // ou "responsable" selon le contexte

        portefeuilleRepository.save(p);
        return ResponseEntity.ok().build();
    }




    @GetMapping("/all")
    public List<Portefeuille> getAll() {
        return portefeuilleService.getALLPortfeuille();
    }
    // ✅ Récupérer un portefeuille par ID
    @GetMapping("/{id}")
    public Optional<Portefeuille> getPortefeuilleById(@PathVariable Long id) {
        return portefeuilleRepository.findById(id);
    }

    // ✅ Supprimer un portefeuille
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePortefeuille(@PathVariable Long id) {
        portefeuilleRepository.deleteById(id);
        return ResponseEntity.ok().build(); // ✅ Réponse vide mais propre
    }


    // ✅ Mettre à jour un portefeuille
    @PutMapping("/updatePortefeuille/{id}")
    public Portefeuille updatePortefeuille(@RequestBody Portefeuille portefeuille, @PathVariable Long id) {
        return portefeuilleService.updatePorteFeuille(portefeuille);
    }
}

