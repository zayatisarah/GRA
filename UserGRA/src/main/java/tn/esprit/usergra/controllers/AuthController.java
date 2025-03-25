package tn.esprit.usergra.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import tn.esprit.usergra.DTO.AuthResponse;
import tn.esprit.usergra.DTO.LoginRequest;
import tn.esprit.usergra.entites.Utilisateur;
import tn.esprit.usergra.entites.JwtUtil;
import tn.esprit.usergra.repositories.UserRepository;
import tn.esprit.usergra.services.imp.UserDetailsServiceImpl;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil,
                          UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        System.out.println("🔍 Tentative de connexion avec : " + request.getUsername());

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Optional<Utilisateur> optionalUser = userRepository.findByUsername(userDetails.getUsername());

            if (optionalUser.isEmpty()) {
                System.out.println("❌ Utilisateur non trouvé !");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Utilisateur non trouvé !");
            }

            Utilisateur user = optionalUser.get();
            String token = jwtUtil.generateToken(user);
            System.out.println("✅ Token généré pour " + user.getUsername());

            return ResponseEntity.ok(new AuthResponse(token, user.getUsername(), user.getRole().name()));

        } catch (Exception e) {
            System.out.println("❌ Échec d'authentification : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Échec d'authentification !");
        }
    }

}
