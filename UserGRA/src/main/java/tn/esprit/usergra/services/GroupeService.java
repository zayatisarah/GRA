package tn.esprit.usergra.services;

import jakarta.transaction.Transactional;
import tn.esprit.usergra.entites.Groupe;

import java.util.List;

public interface GroupeService {
    Groupe addGroupe(Groupe groupe);

    Groupe updateGroupe(Groupe groupe);

    void deleteGroupe(Long id);

    Groupe getGroupeById(Long id);

    List<Groupe> getAllGroupes();

    Groupe findByNom(String nom);
    List<Groupe> findAllWithRessources() ;


}
