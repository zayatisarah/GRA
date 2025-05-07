package tn.esprit.usergra.entites;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Ressource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String router;
    private String nom;
    private String description;
    // Inverse relation avec Habilitation
    @OneToMany(mappedBy = "ressource")
    private List<Habilitation> habilitations;


    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getRouter() { return router; }
    public void setRouter(String router) { this.router = router; }
    public List<Habilitation> getHabilitations() { return habilitations; }
    public void setHabilitations(List<Habilitation> habilitations) { this.habilitations = habilitations; }
}
