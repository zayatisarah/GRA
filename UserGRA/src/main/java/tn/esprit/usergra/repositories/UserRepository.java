package tn.esprit.usergra.repositories;

import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import tn.esprit.usergra.entites.Utilisateur;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository <Utilisateur,Long>{

    @Query("SELECT u FROM Utilisateur u WHERE LOWER(TRIM(u.username)) = LOWER(TRIM(:username))")
    Optional<Utilisateur> findByUsername(@Param("username") String username);


}
