package tn.esprit.usergra.repositories;

import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import tn.esprit.usergra.entites.Groupe;
import tn.esprit.usergra.entites.Utilisateur;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository <Utilisateur,Long>{
    @Query("SELECT u FROM Utilisateur u WHERE LOWER(TRIM(u.matricule)) = LOWER(TRIM(:matricule))")
    Optional<Utilisateur> findByMatricule(@Param("matricule") String matricule);


    @Query("SELECT u FROM Utilisateur u LEFT JOIN FETCH u.groupe LEFT JOIN FETCH u.droits")
    List<Utilisateur> findAllWithGroupeAndDroits();

    @Query("SELECT u FROM Utilisateur u LEFT JOIN FETCH u.groupe LEFT JOIN FETCH u.droits WHERE u.id = :id")
    Optional<Utilisateur> findByIdWithRelations(@Param("id") Long id);

    @Query("SELECT u FROM Utilisateur u " +
            "LEFT JOIN FETCH u.groupe g " +
            "LEFT JOIN FETCH g.habilitations h " +
            "LEFT JOIN FETCH h.ressource r " +
            "WHERE u.matricule = :matricule")
    Optional<Utilisateur> findByMatriculeWithFullAccess(@Param("matricule") String matricule);

    @Query("SELECT u FROM Utilisateur u LEFT JOIN FETCH u.groupe")
    List<Utilisateur> findAllWithGroupes();


}
