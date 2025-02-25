package util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tn.esprit.usergra.entites.JwtUtil;
import tn.esprit.usergra.entites.Utilisateur;
import tn.esprit.usergra.entites.enumr.Role;

import static org.junit.jupiter.api.Assertions.*;

public class JwtUtilTest {
    private JwtUtil jwtUtil;

    @BeforeEach
    public void setUp() {
        jwtUtil = new JwtUtil();
        jwtUtil.init(); // Initialisation de la clé secrète
        System.out.println("Clé secrète initialisée !");
    }

    @Test
    public void testTokenGenerationAndValidation() {
        // Création d'un utilisateur fictif
        Utilisateur user = new Utilisateur();
        user.setEmail("testUser@example.com");
        user.setRole(Role.ROLE_ADMIN);

        // Génération du token
        String token = jwtUtil.generateToken(user);

        // Affichage des logs pour le debug
        System.out.println("Token généré : " + token);
        System.out.println("Email extrait : " + jwtUtil.extractUsername(token));
        System.out.println("Rôle extrait : " + jwtUtil.extractRole(token));
        System.out.println("Validation du Token : " + jwtUtil.validateToken(token, user.getEmail()));

        // Vérifications
        assertNotNull(token, "Le token ne doit pas être null");
        assertEquals(user.getEmail(), jwtUtil.extractUsername(token), "L'email doit correspondre");
        assertEquals("ROLE_ADMIN", jwtUtil.extractRole(token), "Le rôle doit être ROLE_ADMIN");
        assertTrue(jwtUtil.validateToken(token, user.getEmail()), "Le token doit être valide");
    }
}
