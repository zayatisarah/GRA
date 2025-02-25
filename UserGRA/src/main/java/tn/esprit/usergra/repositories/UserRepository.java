package tn.esprit.usergra.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tn.esprit.usergra.entites.Utilisateur;

@Repository
public interface UserRepository extends JpaRepository <Utilisateur,Long>{
    Utilisateur findByEmail(String email);
}
