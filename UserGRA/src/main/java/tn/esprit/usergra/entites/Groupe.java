package tn.esprit.usergra.entites;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor


public class Groupe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    @OneToMany(mappedBy = "groupe")
    @JsonIgnore
    private List<Utilisateur> utilisateurs;


    @OneToMany(mappedBy = "groupe", cascade = CascadeType.ALL)
    @JsonIncludeProperties({ "id", "ressource" }) // Affiche uniquement les ressources dans les habilitations
    private List<Habilitation> habilitations;


    public Long getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public List<Utilisateur> getUtilisateurs() {
        return utilisateurs;
    }

    // --- Setters ---
    public void setId(Long id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setUtilisateurs(List<Utilisateur> utilisateurs) {
        this.utilisateurs = utilisateurs;
    }

}
