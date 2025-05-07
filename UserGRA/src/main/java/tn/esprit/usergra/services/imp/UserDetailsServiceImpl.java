package tn.esprit.usergra.services.imp;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tn.esprit.usergra.entites.Utilisateur;
import tn.esprit.usergra.repositories.UserRepository;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional



    public UserDetails loadUserByUsername(String matricule) throws UsernameNotFoundException {
        Utilisateur user = userRepository.findByMatricule(matricule)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable"));

        // 🔒 Bloquer si inactif
        if (!user.isActif()) {
            throw new LockedException("Compte bloqué !");
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getMatricule())
                .password(user.getPassword())
                .authorities(user.getRole().name())
                .build();
    }


}
