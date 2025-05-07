package tn.esprit.usergra.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tn.esprit.usergra.entites.Groupe;

import java.util.List;
import java.util.Optional;

public interface GroupeRepository extends JpaRepository<Groupe,Long> {
    Optional<Object> findByNom(String nom);
    @Query("SELECT DISTINCT g FROM Groupe g LEFT JOIN FETCH g.habilitations h LEFT JOIN FETCH h.ressource")
    List<Groupe> findAllWithHabilitations();

}
