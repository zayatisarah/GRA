package util;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tn.esprit.usergra.entites.JwtUtil;
import tn.esprit.usergra.entites.Utilisateur;
import tn.esprit.usergra.entites.enumr.Role;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;

public class JwtUtilTest {
    private JwtUtil jwtUtil;

    @BeforeEach
    public void setUp() {
        jwtUtil = new JwtUtil(); // ✅ Initialisation correcte
        System.out.println("🔑 Clé secrète initialisée !");
    }

    private String extractRole(String token) {
        Claims claims = jwtUtil.extractAllClaims(token);
        return claims.get("role", String.class);
    }

    @Test
    public void testTokenGenerationAndValidation() {
        // ✅ Création d'un utilisateur fictif
        Utilisateur user = new Utilisateur();
        user.setUsername("admin");
        user.setEmail("admin@gmail.com");
        user.setPassword("sarah123");
        user.setRole(Role.ROLE_ADMIN);

        // ✅ Convertir `Utilisateur` en `UserDetails` car `validateToken` attend un `UserDetails`
        UserDetails userDetails = User.withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().name().replace("ROLE_", "")) // Convertir en format `UserDetails`
                .build();

        // ✅ Génération du token
        String token = jwtUtil.generateToken(user);

        // ✅ Affichage des logs pour le debug
        System.out.println("🟢 Token généré : " + token);
        System.out.println("📧 Username extrait : " + jwtUtil.extractUsername(token));
        System.out.println("🔑 Rôle extrait : " + extractRole(token));

        // ✅ Vérifications
        assertNotNull(token, "Le token ne doit pas être null");
        assertEquals(user.getUsername(), jwtUtil.extractUsername(token), "Le username doit correspondre");
        assertEquals("ROLE_ADMIN", extractRole(token), "Le rôle doit être ROLE_ADMIN");

        // ✅ Correction : utiliser `userDetails` dans `validateToken`
        assertTrue(jwtUtil.validateToken(token, userDetails), "Le token doit être valide");
    }
}
