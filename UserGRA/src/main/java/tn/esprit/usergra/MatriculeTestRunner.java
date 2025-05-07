package tn.esprit.usergra;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import tn.esprit.usergra.entites.Utilisateur;
import tn.esprit.usergra.repositories.UserRepository;

@Component
public class MatriculeTestRunner implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("🔥 Classe chargée !");
        System.out.println("🔁 Test findByMatricule(\"ADM001\")");

        userRepository.findByMatricule("ADM001").ifPresentOrElse(
                user -> System.out.println("✅ Utilisateur trouvé : " + user.getUsername()),
                () -> System.out.println("❌ Aucun utilisateur trouvé avec ADM001")
        );
    }
}