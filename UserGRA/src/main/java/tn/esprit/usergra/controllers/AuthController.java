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
import tn.esprit.usergra.DTO.UtilisateurLoginResponseDTO;
import tn.esprit.usergra.entites.Groupe;
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
    public ResponseEntity<UtilisateurLoginResponseDTO> login(@RequestBody LoginRequest request) {
        Utilisateur user = userRepository.findByMatricule(request.getMatricule())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // ❌ Vérifie si le compte est actif
        if (!user.isActif()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(null); // ou un DTO avec un message comme "Compte désactivé"
        }

        // 🛡️ Vérifie le mot de passe
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = jwtService.generateToken(user);

        UtilisateurLoginResponseDTO response = new UtilisateurLoginResponseDTO();
        response.setToken(token);
        response.setMatricule(user.getMatricule());
        response.setRole(user.getRole().name());
        response.setFirstLogin(user.isFirstLogin());

        Groupe groupe = user.getGroupe();
        if (groupe != null) {
            UtilisateurLoginResponseDTO.GroupeDTO groupeDTO = new UtilisateurLoginResponseDTO.GroupeDTO();
            groupeDTO.setNom(groupe.getNom());

            List<UtilisateurLoginResponseDTO.HabilitationDTO> habilitationDTOs = groupe.getHabilitations().stream()
                    .map(h -> {
                        UtilisateurLoginResponseDTO.HabilitationDTO hDTO = new UtilisateurLoginResponseDTO.HabilitationDTO();
                        UtilisateurLoginResponseDTO.RessourceDTO rDTO = new UtilisateurLoginResponseDTO.RessourceDTO();
                        rDTO.setNom(h.getRessource().getNom());
                        rDTO.setRouter(h.getRessource().getRouter());
                        hDTO.setRessource(rDTO);
                        return hDTO;
                    })
                    .toList();

            groupeDTO.setHabilitations(habilitationDTOs);
            response.setGroupe(groupeDTO);
        }

        return ResponseEntity.ok(response);
    }


    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        String matricule = request.get("matricule");

        Utilisateur user = userRepository.findByMatriculeWithFullAccess(matricule)
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


