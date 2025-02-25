package tn.esprit.usergra;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String rawPassword = "sarah1234"; // Mets ton mot de passe ici
        String hashedPassword = encoder.encode(rawPassword);

        System.out.println("Mot de passe haché : " + hashedPassword);
    }
}
