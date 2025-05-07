package com.example.client.controllers;

import com.example.client.entites.Actionnaire;
import com.example.client.repositories.ActionnaireRepository;
import com.example.client.services.ActionnaireService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController

@RequestMapping ("/actionnaire")

public class ActionnaireController {

    private final ActionnaireService actionnaireService;
    private final ActionnaireRepository actionnaireRepository;

    @Autowired
    public ActionnaireController(ActionnaireService actionnaireService, ActionnaireRepository actionnaireRepository) {
        this.actionnaireService = actionnaireService;
        this.actionnaireRepository = actionnaireRepository;
    }

    // ✅ Tester la connexion à la base de données
    @GetMapping ("/ping")
    public String testDatabaseConnection() {
        return "Connexion à la base de données réussie pour les actionnaires !";
    }

    @PostMapping("/add")
    public Actionnaire addActionnaire(@Valid @RequestBody Actionnaire actionnaire) {

        // Sauvegarder l'actionnaire dans la base de données
        return actionnaireRepository.save(actionnaire);
    }


    // ✅ Récupérer tous les actionnaires
    @GetMapping("/all")
    public List<Actionnaire> getAllActionnaires() {
        return actionnaireRepository.findAll();
    }

    // ✅ Récupérer un actionnaire par ID
    @GetMapping("/{id}")
    public Optional<Actionnaire> getActionnaireById(@PathVariable Long id) {
        return actionnaireRepository.findById(id);
    }

    // ✅ Supprimer un actionnaire
    @DeleteMapping("/{id}")
    public String deleteActionnaire(@PathVariable Long id) {
        actionnaireRepository.deleteById(id);
        return "Actionnaire supprimé avec succès !";
    }

    // ✅ Mettre à jour un actionnaire
    @PutMapping("/update/{id}")
    public Actionnaire updateActionnaire(@RequestBody Actionnaire actionnaire, @PathVariable Long id) {
        return actionnaireService.updateActionnaire(actionnaire);
    }

}

