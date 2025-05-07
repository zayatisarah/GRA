package tn.esprit.usergra.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import tn.esprit.usergra.entites.Utilisateur;
import tn.esprit.usergra.repositories.UserRepository;

import java.util.Map;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public AdminController(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PutMapping("/reset-password/{matricule}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> resetPasswordByAdmin(@PathVariable String matricule) {
        Utilisateur user = userRepository.findByMatricule(matricule)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        user.setPassword(passwordEncoder.encode("1"));
        user.setFirstLogin(true);
        userRepository.save(user);

        return ResponseEntity.ok(Map.of("message", "Mot de passe réinitialisé à 1 avec succès."));
    }
}
