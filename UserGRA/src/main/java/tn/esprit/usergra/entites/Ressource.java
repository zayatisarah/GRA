package tn.esprit.usergra.entites;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ressource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String router;
    private String nom;
    private String description;

    @OneToMany(mappedBy = "ressource")
    @JsonIgnore
    private Set<Habilitation> habilitations;
}
