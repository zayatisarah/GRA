package tn.esprit.usergra.services.imp;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import tn.esprit.usergra.entites.Utilisateur;
import tn.esprit.usergra.repositories.UserRepository;
import tn.esprit.usergra.services.UserService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImp(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Utilisateur addUser(Utilisateur user) {
        user.setPassword(passwordEncoder.encode(user.getPassword())); // 🔒 Hachage du mot de passe avant stockage
        return userRepository.save(user);
    }

    public void testFindUser(String username) {
        System.out.println("🔍 [TEST] Recherche de l'utilisateur avec username : " + username);

        Optional<Utilisateur> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isPresent()) {
            Utilisateur foundUser = optionalUser.get();
            System.out.println("✅ [TEST] Utilisateur trouvé en base !");
            System.out.println("📌 Username: " + foundUser.getUsername());
            System.out.println("📧 Email: " + foundUser.getEmail());
            System.out.println("🔒 Password Hash: " + foundUser.getPassword());
            System.out.println("🎭 Role: " + foundUser.getRole());
        } else {
            System.out.println("❌ [TEST] Aucun utilisateur trouvé avec username : " + username);
        }
    }


    @Override
    public Utilisateur authenticate(String username, String password) {
        System.out.println("🔍 Tentative d'authentification pour : " + username);

        // 🔎 Test de recherche dans la base de données
        testFindUser(username);

        Optional<Utilisateur> optionalUser = userRepository.findByUsername(username);

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
    public Utilisateur deleteUser(long id) {
        userRepository.deleteById(id);
        return null;
    }

    @Override
    public List<Utilisateur> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public Utilisateur updateUser(Utilisateur user) {
        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("🔎 Tentative de chargement de l'utilisateur avec username : " + username);
        System.out.println("🔍 Vérification en base pour username : [" + username + "]");

        Optional<Utilisateur> optionalUser = userRepository.findByUsername(username.trim().toLowerCase());

        if (optionalUser.isEmpty()) {
            System.out.println("❌ Utilisateur non trouvé pour username : " + username);
            throw new UsernameNotFoundException("User not found");
        }

        Utilisateur user = optionalUser.get();
        System.out.println("✅ Utilisateur trouvé : " + user.getUsername() + " - " + user.getEmail());

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singletonList(() -> user.getRole().name())
        );
    }
    @PersistenceContext
    private EntityManager entityManager;

    public void testQuery() {
        String sql = "SELECT u FROM Utilisateur u WHERE LOWER(TRIM(u.username)) = LOWER(TRIM(:username))";
        List<Utilisateur> results = entityManager.createQuery(sql, Utilisateur.class)
                .setParameter("username", "kiko")
                .getResultList();

        System.out.println("🔍 Résultat de la requête manuelle Hibernate : " + (results.isEmpty() ? "❌ Aucun utilisateur trouvé" : "✅ Utilisateur trouvé !"));
    }

}
