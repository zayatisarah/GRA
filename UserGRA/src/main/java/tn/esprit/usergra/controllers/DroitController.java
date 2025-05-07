package tn.esprit.usergra.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tn.esprit.usergra.entites.Droit;
import tn.esprit.usergra.repositories.DroitRepository;

import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/droit")
@RequiredArgsConstructor
public class DroitController {

    private final DroitRepository droitRepository;

    // ✅ Récupérer tous les droits (accès admin uniquement)
    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<Droit>> getAllDroits() {
        List<Droit> droits = droitRepository.findAll();
        return ResponseEntity.ok(droits);
    }

    // ✅ Ajouter un droit (ex: "supprimer", "écrire", etc.)
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Droit> addDroit(@RequestBody Droit droit) {
        Droit saved = droitRepository.save(droit);
        return ResponseEntity.ok(saved);
    }

    // ✅ Supprimer un droit
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> deleteDroit(@PathVariable Long id) {
        droitRepository.deleteById(id);
        return ResponseEntity.ok("✅ Droit supprimé");
    }
}
