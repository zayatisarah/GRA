package tn.esprit.usergra.DTO;

import lombok.Data;

@Data
public class LoginRequest {
    private String matricule;
    private String password;
}