package tn.esprit.usergra.controllers;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.web.bind.annotation.*;
import tn.esprit.usergra.entites.Utilisateur;

import tn.esprit.usergra.repositories.UserRepository;
import tn.esprit.usergra.services.UserService;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

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
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return "Utilisateur supprimé avec succès !";
    }
    @PutMapping("updateUser/{id}")
    Utilisateur UpdateUser(@RequestBody Utilisateur user ,@PathVariable Long id){
        return userService.updateUser(user );
    }

}
