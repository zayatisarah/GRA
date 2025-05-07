package tn.esprit.usergra.services.imp;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import tn.esprit.usergra.entites.Groupe;
import tn.esprit.usergra.entites.UpdatePasswordRequest;
import tn.esprit.usergra.entites.Utilisateur;
import tn.esprit.usergra.entites.enumr.Role;
import tn.esprit.usergra.repositories.GroupeRepository;
import tn.esprit.usergra.repositories.UserRepository;
import tn.esprit.usergra.services.EmailService;
import tn.esprit.usergra.services.UserService;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private EmailService emailService;


    private final GroupeRepository groupeRepository;
    @Autowired
    public UserServiceImp(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, GroupeRepository groupeRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.groupeRepository = groupeRepository;
    }



    @Override
    public Utilisateur addUser(Utilisateur user) {
        // 1. Encoder le mot de passe
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode("defaultPassword"));
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }


        // 2. Lier le groupe (si présent)
        if (user.getGroupe() != null && user.getGroupe().getId() != null) {
            Groupe groupe = groupeRepository.findById(user.getGroupe().getId())
                    .orElseThrow(() -> new RuntimeException("❌ Groupe introuvable avec ID : " + user.getGroupe().getId()));
            user.setGroupe(groupe);
        }

        // 3. Sauvegarder
        Utilisateur saved = userRepository.save(user);

        // 4. Envoyer l'e-mail si Responsable
        if (saved.getRole().equals(Role.ROLE_RESPONSABLE)) {
            emailService.sendWelcomeEmail(saved.getEmail(), saved.getMatricule(), "1");
        }

        return saved;
    }






    public void testFindUser(String matricule) {
        System.out.println("🔍 [TEST] Recherche de l'utilisateur avec username : " + matricule);

        Optional<Utilisateur> optionalUser = userRepository.findByMatricule(matricule);

        if (optionalUser.isPresent()) {
            Utilisateur foundUser = optionalUser.get();
            System.out.println("✅ [TEST] Utilisateur trouvé en base !");
            System.out.println("📌 Username: " + foundUser.getUsername());
            System.out.println("📧 Email: " + foundUser.getEmail());
            System.out.println("🔒 Password Hash: " + foundUser.getPassword());
            System.out.println("🎭 Role: " + foundUser.getRole());
            System.out.println("📌 matricule: " + foundUser.getMatricule());
        } else {
            System.out.println("❌ [TEST] Aucun utilisateur trouvé avec username : " + matricule);
        }
    }


    @Override
    @Transactional

    public Utilisateur authenticate(String matricule, String password) {
        System.out.println("🔍 Tentative d'authentification pour : " + matricule);

        // 🔎 Test de recherche dans la base de données
        testFindUser(matricule);

        Optional<Utilisateur> optionalUser = userRepository.findByMatricule(matricule);

        if (optionalUser.isPresent()) {
            Utilisateur user = optionalUser.get();
            System.out.println("✅ Utilisateur trouvé : " + user.getUsername());

            if (passwordEncoder.matches(password, user.getPassword())) {
                System.out.println("✅ Mot de passe correct !");
                return user;
            } else {
                System.out.println("❌ Mot de passe incorrect !");
            }

        }

        return null;
    }

    @Override
    @Transactional

    public Utilisateur deleteUser(long id) {
        userRepository.deleteById(id);
        return null;
    }

    @Override
    @Transactional

    public List<Utilisateur> getAllUser() {
        return userRepository.findAll();
    }


    @Override
    @Transactional
    public void updatePassword(String matricule, UpdatePasswordRequest request) {
        Utilisateur utilisateur = userRepository.findByMatricule(matricule)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable"));

        // Vérifier ancien mot de passe
        if (!passwordEncoder.matches(request.getOldPassword(), utilisateur.getPassword())) {
            throw new IllegalArgumentException("Ancien mot de passe incorrect !");
        }

        // Vérifier la confirmation
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Le mot de passe et la confirmation ne correspondent pas !");
        }

        // Mettre à jour le mot de passe
        utilisateur.setPassword(passwordEncoder.encode(request.getNewPassword()));
        utilisateur.setFirstLogin(false); // Si applicable
        userRepository.save(utilisateur);

        System.out.println("🔐 Mot de passe mis à jour pour : " + matricule);
    }







    @Override
    @Transactional

    public Utilisateur getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'id : " + id));
    }

    @Override
    public void save(Utilisateur user) {
        // 1. Encodage du mot de passe si présent et non vide
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
        }

        // 2. Gestion du groupe s’il est fourni
        if (user.getGroupe() != null && user.getGroupe().getId() != null) {
            Groupe groupe = groupeRepository.findById(user.getGroupe().getId())
                    .orElseThrow(() -> new RuntimeException("❌ Groupe introuvable"));
            user.setGroupe(groupe);
        }

        // 3. Sauvegarde
        userRepository.save(user);
    }



    @PersistenceContext
    private EntityManager entityManager;


}
