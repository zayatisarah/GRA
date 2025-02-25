package tn.esprit.usergra.services.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import tn.esprit.usergra.entites.Utilisateur;
import tn.esprit.usergra.repositories.UserRepository;
import tn.esprit.usergra.services.UserService;

import java.util.List;

@Service

public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    @Autowired
    public UserServiceImp(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public Utilisateur addUser(Utilisateur user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
    public Utilisateur authenticate(String email, String password) {
        Utilisateur user = userRepository.findByEmail(email);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    @Override
    public Utilisateur deleteUser(long id) {
        userRepository.deleteById(id);
        return null;
    }

    @Override
    public List<Utilisateur> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public Utilisateur updateUser(Utilisateur user) {
        return userRepository.save(user);
    }
}
