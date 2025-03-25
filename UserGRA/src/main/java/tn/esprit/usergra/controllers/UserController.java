package tn.esprit.usergra.controllers;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.usergra.entites.Utilisateur;

import tn.esprit.usergra.repositories.UserRepository;
import tn.esprit.usergra.services.JwtService;
import tn.esprit.usergra.services.UserService;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
private final JwtService jwtService;
    private final UserService userService;
    private final UserRepository userRepository;

    // ✅ Tester la connexion à la base de données
    @GetMapping("/ping")
    public String testDatabaseConnection() {
        return "Connexion à la base de données réussie !";
    }

    // ✅ Ajouter un utilisateur
    @PostMapping("/add")
    public Utilisateur addUser(@RequestBody Utilisateur user) {
        return userRepository.save(user);
    }

    // ✅ Récupérer tous les utilisateurs
    @GetMapping("/all")
    public List<Utilisateur> getAllUsers() {
        return userRepository.findAll();
    }

    // ✅ Récupérer un utilisateur par ID
    @GetMapping("/{id}")
    public Optional<Utilisateur> getUserById(@PathVariable Long id) {
        return userRepository.findById(id);
    }

    // ✅ Supprimer un utilisateur
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        System.out.println("🛠 Suppression de l'utilisateur avec ID : " + id);

        Optional<Utilisateur> user = userRepository.findById(id);
        if (user.isPresent()) {
            userRepository.deleteById(id);
            return ResponseEntity.ok("Utilisateur supprimé avec succès !");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur non trouvé");
        }
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody Utilisateur user,
                                        @RequestHeader(value = "Authorization", required = false) String authHeader) {

        System.out.println("🔍 Token reçu dans updateUser : " + authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("❌ Aucun token reçu !");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: Missing or invalid token");
        }

        String token = authHeader.substring(7);
        boolean isValidToken = jwtService.validateToken(token);

        if (!isValidToken) {
            System.out.println("❌ Token invalide !");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: Invalid token");
        }

        Optional<Utilisateur> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            Utilisateur updatedUser = existingUser.get();
            updatedUser.setEmail(user.getEmail());
            updatedUser.setRole(user.getRole());
            userRepository.save(updatedUser);

            System.out.println("✅ Utilisateur mis à jour avec succès !");
            return ResponseEntity.ok("Utilisateur mis à jour avec succès !");
        } else {
            System.out.println("❌ Utilisateur introuvable !");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur non trouvé");
        }
    }

}
