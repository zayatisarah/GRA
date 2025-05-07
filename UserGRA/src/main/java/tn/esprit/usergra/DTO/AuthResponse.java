package tn.esprit.usergra.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
@Getter
@Setter
public class AuthResponse {
    private String token;
    private String matricule;
    private String role;
    private boolean firstLogin;

}


