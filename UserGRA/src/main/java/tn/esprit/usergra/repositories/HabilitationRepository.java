package tn.esprit.usergra.repositories;

import feign.Param;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import tn.esprit.usergra.entites.Groupe;
import tn.esprit.usergra.entites.Habilitation;
import tn.esprit.usergra.entites.Ressource;

import java.util.List;

public interface HabilitationRepository extends JpaRepository<Habilitation,Long> {
    boolean existsByGroupeAndRessource(Groupe groupe, Ressource ressource);

    List<Habilitation> findByGroupe(Groupe groupe);
    @Modifying
    @Transactional
    @Query("DELETE FROM Habilitation h WHERE h.groupe.id = :groupeId")
    void deleteByGroupeId(@Param("groupeId") Long groupeId);

}
