package tn.esprit.usergra.entites;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;
@Entity
public class Habilitation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Groupe groupe;

    @ManyToOne
    @JsonIncludeProperties({ "id", "nom", "router" }) // Pour afficher uniquement ce qu’il faut

    private Ressource ressource;

    public Long getId() {
        return id;
    }

    public Groupe getGroupe() {
        return groupe;
    }

    public void setGroupe(Groupe groupe) {
        this.groupe = groupe;
    }

    public Ressource getRessource() {
        return ressource;
    }

    public void setRessource(Ressource ressource) {
        this.ressource = ressource;
    }
}

