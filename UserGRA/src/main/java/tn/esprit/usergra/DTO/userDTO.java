package tn.esprit.usergra.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import tn.esprit.usergra.entites.Utilisateur;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Data
public class userDTO {

    private Long id;
    private String matricule;
    private String username;
    private String email;
    private String role;
    private boolean actif;
    private boolean firstLogin;
    private Long groupeId;
    private String groupeNom;
    private List<Long> droitIds;

    // ✅ Constructeur vide nécessaire pour Jackson
    public userDTO() {}

    // ✅ Constructeur depuis Utilisateur
    public userDTO(Utilisateur user) {
        this.id = user.getId();
        this.matricule = user.getMatricule();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.role = user.getRole().name();
        this.actif = user.isActif();
        this.firstLogin = user.isFirstLogin();
        this.groupeId = user.getGroupe() != null ? user.getGroupe().getId() : null;
        this.groupeNom = user.getGroupe() != null ? user.getGroupe().getNom() : null;
        this.droitIds = user.getDroits() != null
                ? user.getDroits().stream().map(d -> d.getId()).collect(Collectors.toList())
                : List.of();
    }
}
