package tn.esprit.usergra.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import tn.esprit.usergra.DTO.AuthResponse;
import tn.esprit.usergra.DTO.LoginRequest;
import tn.esprit.usergra.entites.Utilisateur;
import tn.esprit.usergra.repositories.UserRepository;
import tn.esprit.usergra.services.EmailService;
import tn.esprit.usergra.services.JwtService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private EmailService emailService;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtService jwtService,
                          UserRepository userRepository,
                          BCryptPasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        System.out.println("🔍 Tentative de connexion avec : " + request.getMatricule());

        try {
            System.out.println("🔑 AuthManager - Authentification...");
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getMatricule(), request.getPassword())
            );

            System.out.println("✅ Authentifié !");
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            System.out.println("🔍 Recherche user en base : " + userDetails.getUsername());
            Optional<Utilisateur> optionalUser = userRepository.findByMatricule(userDetails.getUsername());

            if (optionalUser.isEmpty()) {
                System.out.println("❌ Utilisateur non trouvé !");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Utilisateur non trouvé !");
            }

            Utilisateur user = optionalUser.get();
            Map<String, Object> claims = new HashMap<>();
            claims.put("authorities", List.of(user.getRole().name()));
            claims.put("firstLogin", user.isFirstLogin());

            System.out.println("🛡️ Génération token...");
            String token = jwtService.generateToken(claims, user);
            System.out.println("🪪 Token généré : " + token);

            return ResponseEntity.ok(
                    new AuthResponse(token, user.getMatricule(), user.getRole().name(), user.isFirstLogin())
            );

        } catch (Exception e) {
            e.printStackTrace(); // 🔍 Pour voir l'erreur complète dans ta console backend
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur serveur : " + e.getMessage()); // 🔔 Le message est aussi renvoyé au front
        }


    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        String matricule = request.get("matricule");

        Utilisateur user = userRepository.findByMatricule(matricule)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));

        // ⚠️ Ne rien modifier ici dans la base de données !

        String adminEmail = "admin@example.com";
        String subject = "Demande de réinitialisation de mot de passe";
        String body = "Bonjour Admin,\n\nL'utilisateur " + user.getMatricule() + " (" + user.getEmail() + ")" +
                " a demandé une réinitialisation de son mot de passe.\n\nVeuillez effectuer la réinitialisation si nécessaire.";

        emailService.sendEmail(adminEmail, subject, body);

        return ResponseEntity.ok(Map.of("message", "Votre demande a été transmise à l’administrateur."));
    }



}


