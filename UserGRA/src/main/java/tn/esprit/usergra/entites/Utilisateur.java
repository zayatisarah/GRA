package tn.esprit.usergra.entites;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import tn.esprit.usergra.entites.enumr.Role;

@Entity
@Table(name = "UTILISATEUR") // Correspond au nom réel de la table en BDD
@Component
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")  // Auto-incrémentation (associé au trigger en SQL)
    private Long id;

    @Column(name = "NOM", nullable = false) // Spécifier le mapping exact
    private String nom;

    @Column(name = "PRENOM", nullable = false) // Corriger la majuscule et éviter l'erreur
    private String prenom;

    @Column(name = "EMAIL", nullable = false, unique = true) // Ajout de `unique = true` pour éviter les doublons
    private String email;

    @Column(name = "MOT_DE_PASSE", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE", nullable = false)
    private Role role;
}
