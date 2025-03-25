package tn.esprit.usergra.services;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import tn.esprit.usergra.entites.Utilisateur;

import java.util.List;

public interface UserService {
    Utilisateur addUser(Utilisateur utilisateur);
    Utilisateur deleteUser(long id );
    List<Utilisateur> getAllUser();
    Utilisateur updateUser(Utilisateur user);

    Utilisateur authenticate(String username, String password);

    UserDetails loadUserByUsername(String email) throws UsernameNotFoundException;
}
