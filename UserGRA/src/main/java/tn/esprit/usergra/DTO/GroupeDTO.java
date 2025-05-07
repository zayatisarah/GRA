package tn.esprit.usergra.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GroupeDTO {
    private String nom;
    private List<Long> ressourceIds;
    private List<Long> utilisateurIds; // ✅ Ajout pour l'affectation des utilisateurs

    // ✅ Si tu veux garder les getters/setters manuels :
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public List<Long> getRessourceIds() {
        return ressourceIds;
    }

    public void setRessourceIds(List<Long> ressourceIds) {
        this.ressourceIds = ressourceIds;
    }

    public List<Long> getUtilisateurIds() {
        return utilisateurIds;
    }

    public void setUtilisateurIds(List<Long> utilisateurIds) {
        this.utilisateurIds = utilisateurIds;
    }
}
