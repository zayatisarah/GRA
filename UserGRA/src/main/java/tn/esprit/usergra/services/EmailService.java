package tn.esprit.usergra.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendWelcomeEmail(String to, String matricule, String motDePasse) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Bienvenue sur la plateforme");
            message.setText(
                    "Bonjour,\n\nVotre compte a été créé avec succès.\n" +
                            "Matricule : " + matricule + "\n" +
                            "Mot de passe initial : " + motDePasse + "\n\n" +
                            "Merci de le modifier dès votre première connexion."
            );

            mailSender.send(message);
            System.out.println("✅ Email envoyé à " + to);  // LOG ICI
        } catch (Exception e) {
            System.err.println("❌ Erreur envoi email : " + e.getMessage());
        }
    }

    public void sendEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
            System.out.println("📨 Email envoyé à " + to);
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de l'envoi de l'email à " + to + " : " + e.getMessage());
        }
    }

}

