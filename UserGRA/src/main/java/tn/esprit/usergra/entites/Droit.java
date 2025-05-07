package tn.esprit.usergra.entites;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Droit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom; // ex: MODIFIER, SUPPRIMER, CREER

    @ManyToMany(mappedBy = "droits")
    @JsonIgnore
    private List<Utilisateur> utilisateurs;

}
