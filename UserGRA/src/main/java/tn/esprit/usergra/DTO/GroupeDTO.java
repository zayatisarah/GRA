package tn.esprit.usergra.DTO;

import lombok.Getter;
import lombok.Setter;
import tn.esprit.usergra.entites.Groupe;
import tn.esprit.usergra.entites.Habilitation;
import tn.esprit.usergra.entites.Utilisateur;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class GroupeDTO {
    private Long id;
    private String nom;
    private List<Long> ressourceIds;
    private List<String> ressourcesRouters;
    private List<Long> utilisateurIds;
    private List<String> utilisateursMatricules;
    private List<HabilitationDTO> habilitations;

    // ✅ Ce constructeur est indispensable pour la désérialisation
    public GroupeDTO() {
    }

    public GroupeDTO(Groupe g) {
        this.id = g.getId();
        this.nom = g.getNom();

        if (g.getHabilitations() != null) {
            this.ressourceIds = g.getHabilitations().stream()
                    .map(h -> h.getRessource().getId())
                    .collect(Collectors.toList());

            this.ressourcesRouters = g.getHabilitations().stream()
                    .map(h -> h.getRessource().getRouter())
                    .collect(Collectors.toList());

            this.habilitations = g.getHabilitations().stream()
                    .map(HabilitationDTO::new)
                    .collect(Collectors.toList());
        } else {
            this.ressourceIds = Collections.emptyList();
            this.ressourcesRouters = Collections.emptyList();
            this.habilitations = Collections.emptyList();
        }

        if (g.getUtilisateurs() != null) {
            this.utilisateurIds = g.getUtilisateurs().stream()
                    .map(Utilisateur::getId)
                    .collect(Collectors.toList());

            this.utilisateursMatricules = g.getUtilisateurs().stream()
                    .map(Utilisateur::getMatricule)
                    .collect(Collectors.toList());
        } else {
            this.utilisateurIds = Collections.emptyList();
            this.utilisateursMatricules = Collections.emptyList();
        }
    }
}
