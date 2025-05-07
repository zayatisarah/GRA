package tn.esprit.usergra.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import tn.esprit.usergra.PasswordEncoderGenerator;
import tn.esprit.usergra.entites.Droit;
import tn.esprit.usergra.entites.Groupe;
import tn.esprit.usergra.entites.UpdatePasswordRequest;
import tn.esprit.usergra.entites.Utilisateur;

import tn.esprit.usergra.entites.enumr.Role;
import tn.esprit.usergra.repositories.DroitRepository;
import tn.esprit.usergra.repositories.GroupeRepository;
import tn.esprit.usergra.repositories.UserRepository;
import tn.esprit.usergra.services.EmailService;
import tn.esprit.usergra.services.JwtService;
import tn.esprit.usergra.services.UserService;
import tn.esprit.usergra.DTO.userDTO;

import java.util.*;

@RestController
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
private final JwtService jwtService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final GroupeRepository groupeRepository;
  @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private DroitRepository droitRepository;@Autowired
    private EmailService emailService;

    // MessageResponse.java
    public record MessageResponse(String message) {}

    // ✅ Tester la connexion à la base de données
    @GetMapping("/ping")
    public String testDatabaseConnection() {
        return "Connexion à la base de données réussie !";
    }

    // ✅ Ajouter un utilisateur
    @PostMapping("/add")
    public ResponseEntity<?> addUser(@RequestBody userDTO dto) {
        try {
            Utilisateur user = new Utilisateur();
            user.setUsername(dto.getUsername());
            user.setEmail(dto.getEmail());
            user.setMatricule(dto.getMatricule());
            user.setRole(Role.valueOf(dto.getRole()));
            user.setActif(true);
            user.setPassword(passwordEncoder.encode("1")); // mot de passe initial

            // Groupe
            if (dto.getGroupeId() != null) {
                Groupe groupe = groupeRepository.findById(dto.getGroupeId())
                        .orElseThrow(() -> new RuntimeException("Groupe non trouvé"));
                user.setGroupe(groupe);
            }

            // Droits
            if (dto.getDroitIds() != null && !dto.getDroitIds().isEmpty()) {
                List<Droit> droits = droitRepository.findAllById(dto.getDroitIds());
                user.setDroits(droits);
            }

            Utilisateur saved = userRepository.save(user);

            if (saved == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("❌ Erreur de sauvegarde !");
            }

            // 📧 Envoi de l'email
            emailService.sendWelcomeEmail(user.getEmail(), user.getMatricule(), "1");

            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("❌ " + e.getMessage());
        }
    }


    // ✅ Récupérer tous les utilisateurs
    @GetMapping("/all")
    public List<Utilisateur> getAllUsers() {
        return userRepository.findAllWithGroupeAndDroits();
    }

    // ✅ Récupérer un utilisateur par ID
    @GetMapping("/{id}")


    public Optional<Utilisateur> getUserById(@PathVariable Long id) {
        return userRepository.findById(id);
    }

    // ✅ Supprimer un utilisateur
    @PutMapping("/toggle-block/{id}")
    public ResponseEntity<?> toggleBlockUser(@PathVariable Long id) {
        Optional<Utilisateur> optionalUser = userRepository.findById(id);

        if (optionalUser.isPresent()) {
            Utilisateur user = optionalUser.get();
            user.setActif(!user.isActif()); // Inverse actif <-> inactif
            userRepository.save(user);

            String status = user.isActif() ? "débloqué" : "bloqué";
            return ResponseEntity.ok("✅ Utilisateur " + status + " avec succès !");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("❌ Utilisateur introuvable");
        }
    }




    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody Utilisateur user) {
        Long id = user.getId();
        Optional<Utilisateur> existingUserOpt = userRepository.findById(id);

        if (existingUserOpt.isPresent()) {
            Utilisateur existingUser = existingUserOpt.get();
            existingUser.setUsername(user.getUsername());
            existingUser.setEmail(user.getEmail());
            existingUser.setRole(user.getRole());
            existingUser.setActif(user.isActif());

            // Mot de passe si présent
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                existingUser.setPassword(user.getPassword());
            }

            // Gestion du groupe
            if (user.getGroupe() != null && user.getGroupe().getId() != null) {
                Groupe groupe = groupeRepository.findById(user.getGroupe().getId())
                        .orElseThrow(() -> new RuntimeException("Groupe non trouvé"));
                existingUser.setGroupe(groupe);
            } else {
                existingUser.setGroupe(null);
            }

            // Gestion des droits
            if (user.getDroitIds() != null && !user.getDroitIds().isEmpty()) {
                List<Droit> droits = droitRepository.findAllById(user.getDroitIds());
                existingUser.setDroits(droits); // ou new HashSet<>(droits) si nécessaire
            }


            userRepository.save(existingUser);
            return ResponseEntity.ok(Map.of("message", "✅ Utilisateur mis à jour avec succès !"));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("❌ Utilisateur non trouvé");
    }




    @PutMapping("/update-password")
    public ResponseEntity<?> updatePassword(@RequestBody UpdatePasswordRequest request, Authentication authentication) {
        String matricule = authentication.getName(); // extrait du token

        System.out.println("🔐 Matricule extrait pour MAJ : " + matricule);

        try {
            userService.updatePassword(matricule, request);
            return ResponseEntity.ok(Map.of("message", "✅ Mot de passe mis à jour !"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("❌ " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("❌ Erreur serveur !");
        }
    }



    @GetMapping("/test-auth")
    public ResponseEntity<?> testAuth(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        System.out.println("🔍 Header: " + authHeader);
        return ResponseEntity.ok("Token reçu !");
    }










}
