package tn.esprit.usergra.services.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tn.esprit.usergra.entites.Utilisateur;
import tn.esprit.usergra.repositories.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("🔍 Recherche de l'utilisateur avec le username : " + username);
        return userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    System.out.println("❌ Utilisateur non trouvé avec le username : " + username);
                    return new UsernameNotFoundException("Utilisateur non trouvé : " + username);
                });
    }
}
