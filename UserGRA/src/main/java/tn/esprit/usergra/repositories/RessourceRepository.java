package tn.esprit.usergra.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.usergra.entites.Habilitation;
import tn.esprit.usergra.entites.Ressource;

public interface RessourceRepository extends JpaRepository<Ressource,Long> {
}
