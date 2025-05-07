package tn.esprit.usergra.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import tn.esprit.usergra.DTO.GroupeDTO;
import tn.esprit.usergra.entites.Groupe;
import tn.esprit.usergra.entites.Habilitation;
import tn.esprit.usergra.entites.Ressource;
import tn.esprit.usergra.entites.Utilisateur;
import tn.esprit.usergra.repositories.GroupeRepository;
import tn.esprit.usergra.repositories.HabilitationRepository;
import tn.esprit.usergra.repositories.RessourceRepository;
import tn.esprit.usergra.services.GroupeService;
import tn.esprit.usergra.services.UserService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/groupe")
@RequiredArgsConstructor
public class GroupeController {

    private final GroupeService groupeService;
    @Autowired
    private final UserService userService;
    private final GroupeRepository groupeRepository;
    @Autowired
    private  RessourceRepository ressourceRepository;
    @Autowired
    private HabilitationRepository habilitationRepository;

    // ✅ Ajouter un groupe
    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_RESPONSABLE')")
    public ResponseEntity<Groupe> addGroupe(@RequestBody GroupeDTO dto) {
        System.out.println("✅ Ajout du groupe avec ressources et utilisateurs");

        Groupe groupe = new Groupe();
        groupe.setNom(dto.getNom());
        Groupe savedGroupe = groupeRepository.save(groupe);

        // 🔁 Affecter les ressources
        if (dto.getRessourceIds() != null) {
            for (Long idRessource : dto.getRessourceIds()) {
                Ressource res = ressourceRepository.findById(idRessource)
                        .orElseThrow(() -> new RuntimeException("❌ Ressource introuvable : " + idRessource));
                Habilitation habilitation = new Habilitation();
                habilitation.setGroupe(savedGroupe);
                habilitation.setRessource(res);
                habilitationRepository.save(habilitation);
            }
        }

        // 👤 Affecter les utilisateurs
        if (dto.getUtilisateurIds() != null) {
            for (Long idUtilisateur : dto.getUtilisateurIds()) {
                Utilisateur user = userService.getById(idUtilisateur);
                user.setGroupe(savedGroupe);
                userService.save(user); // n'oublie que `save` doit être dans ton `UserService`
            }
        }

        return ResponseEntity.ok(savedGroupe);
    }

    @GetMapping("/ping")
    public String testDatabaseConnection() {
        return "Connexion à la base de données réussie !";
    }

    // ✅ Modifier un groupe
    @PutMapping("/update")
    public ResponseEntity<Groupe> updateGroupe(@RequestBody Groupe groupe) {
        return ResponseEntity.ok(groupeService.updateGroupe(groupe));
    }

    // ✅ Supprimer un groupe
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')") // <- ou 'ROLE_RESPONSABLE' selon tes cas d’usage
    public ResponseEntity<String> deleteGroupe(@PathVariable Long id) {
        groupeService.deleteGroupe(id);
        return ResponseEntity.ok("Groupe supprimé avec succès !");
    }


    // ✅ Récupérer un groupe par ID
    @GetMapping("/{id}")
    public ResponseEntity<Groupe> getGroupeById(@PathVariable Long id) {
        return ResponseEntity.ok(groupeService.getGroupeById(id));
    }

    // ✅ Récupérer tous les groupes
    @GetMapping("/all")
    public ResponseEntity<List<Groupe>> getAllGroupes() {
        return ResponseEntity.ok(groupeService.getAllGroupes());
    }

    // ✅ Rechercher un groupe par nom
    @GetMapping("/find/{nom}")
    public ResponseEntity<Groupe> findByNom(@PathVariable String nom) {
        return ResponseEntity.ok(groupeService.findByNom(nom));
    }

    @GetMapping("/groupes-avec-users")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_RESPONSABLE')")

    public ResponseEntity<List<Groupe>> getGroupesAvecUtilisateurs() {
        List<Groupe> groupes = groupeService.getAllGroupes();
        return ResponseEntity.ok(groupes);
    }

    @PutMapping("/assign-ressources/{groupeId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> assignRessourcesToGroupe(
            @PathVariable Long groupeId,
            @RequestBody List<Long> ressourceIds) {

        Groupe groupe = groupeRepository.findById(groupeId)
                .orElseThrow(() -> new RuntimeException("❌ Groupe introuvable avec l’ID : " + groupeId));

        // 🧹 1. Supprimer les habilitations existantes
        habilitationRepository.deleteByGroupeId(groupeId);

        // ✅ 2. Réaffecter uniquement les ressources sélectionnées
        List<Ressource> ressources = ressourceRepository.findAllById(ressourceIds);
        for (Ressource ressource : ressources) {
            Habilitation habilitation = new Habilitation();
            habilitation.setGroupe(groupe);
            habilitation.setRessource(ressource);
            habilitationRepository.save(habilitation);
        }

        return ResponseEntity.ok("✅ Ressources mises à jour avec succès !");
    }



}
