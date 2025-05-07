package tn.esprit.usergra.DTO;

import lombok.Getter;
import lombok.Setter;
import tn.esprit.usergra.entites.Droit;

import java.util.List;
@Getter
@Setter
public class userDTO {
    private String matricule;
    private String username; // ✅ Ajouté
    private String email;
    private String role;
    private Long groupeId;
    private List<Long> droitIds;

    public List<Long> getDroitIds() {
        return droitIds;
    }

    public void setDroitIds(List<Long> droitIds) {
        this.droitIds = droitIds;
    }


    // Constructeur
    public userDTO(String matricule, String username, String email, String role) {
        this.matricule = matricule;
        this.username = username;
        this.email = email;
        this.role = role;
    }

    // Getters et Setters
    public String getMatricule() { return matricule; }
    public void setMatricule(String matricule) { this.matricule = matricule; }

    public String getUsername() { return username; } // ✅ Getter ajouté
    public void setUsername(String username) { this.username = username; } // ✅ Setter ajouté

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public Long getGroupeId() { return groupeId; }
    public void setGroupeId(Long groupeId) { this.groupeId = groupeId; }
}
