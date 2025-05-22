package tn.esprit.usergra.entites;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import tn.esprit.usergra.entites.Habilitation;
import tn.esprit.usergra.entites.Utilisateur;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"habilitations", "utilisateurs"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Groupe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private String nom;

    @OneToMany(mappedBy = "groupe")
    @JsonIgnore
    private Set<Utilisateur> utilisateurs;

    @OneToMany(mappedBy = "groupe", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("groupe")
    private Set<Habilitation> habilitations;
}
