package tn.esprit.usergra.DTO;

import tn.esprit.usergra.entites.Habilitation;

public class HabilitationDTO {
    private Long id;
    private String ressourceRouter;

    public HabilitationDTO(Habilitation h) {
        this.id = h.getId();
        this.ressourceRouter = h.getRessource().getRouter();
    }
}
