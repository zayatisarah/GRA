package tn.esprit.usergra.services;


import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import tn.esprit.usergra.entites.Groupe;
import tn.esprit.usergra.entites.UpdatePasswordRequest;
import tn.esprit.usergra.entites.Utilisateur;

import java.util.List;

public interface UserService {
    Utilisateur addUser(Utilisateur utilisateur);
    Utilisateur deleteUser(long id );
    List<Utilisateur> getAllUser();
    void updatePassword(String matricule, UpdatePasswordRequest request);

    Utilisateur authenticate(String username, String password);


    Utilisateur getById(Long id);


    void save(Utilisateur user);

}
