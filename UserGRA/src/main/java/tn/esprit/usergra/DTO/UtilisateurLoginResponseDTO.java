package tn.esprit.usergra.DTO;

import lombok.Data;
import java.util.List;

@Data
public class UtilisateurLoginResponseDTO {
    private String token;
    private String role;
    private String matricule;
    private boolean firstLogin;
    private GroupeDTO groupe;

    @Data
    public static class GroupeDTO {
        private String nom;
        private List<HabilitationDTO> habilitations;
    }

    @Data
    public static class HabilitationDTO {
        private RessourceDTO ressource;
    }

    @Data
    public static class RessourceDTO {
        private String nom;
        private String router;
    }
}
