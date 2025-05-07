package tn.esprit.usergra;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String rawPassword = "kiko"; // 🔑 Le mot de passe que tu veux tester
        String hash = "$2a$10$5k0Ox/RhON70u4x5KOxlV.pWUQlYECYYcEKNgG0ppCYo3rIuLGz.O";

        boolean match = encoder.matches(rawPassword, hash);

        if (match) {
            System.out.println("✅ Mot de passe valide !");
        } else {
            System.out.println("❌ Mot de passe incorrect.");
        }
    }
}
