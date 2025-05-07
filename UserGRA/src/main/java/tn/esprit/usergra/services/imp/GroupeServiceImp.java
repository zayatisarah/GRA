package tn.esprit.usergra.services.imp;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.usergra.entites.Groupe;
import tn.esprit.usergra.entites.Habilitation;
import tn.esprit.usergra.entites.Utilisateur;
import tn.esprit.usergra.repositories.GroupeRepository;
import tn.esprit.usergra.repositories.HabilitationRepository;
import tn.esprit.usergra.repositories.UserRepository;
import tn.esprit.usergra.services.GroupeService;

import java.util.List;
@Service
@RequiredArgsConstructor
public class GroupeServiceImp implements GroupeService {
   @Autowired
    private  GroupeRepository groupeRepository;
    @Autowired
    private HabilitationRepository habilitationRepository;

    @Autowired
    private UserRepository utilisateurRepository;

    @Override
    @Transactional

    public Groupe addGroupe(Groupe groupe) {
        return groupeRepository.save(groupe);
    }

    @Override
    @Transactional

    public Groupe updateGroupe(Groupe groupe) {
        if (groupe.getId() == null || !groupeRepository.existsById(groupe.getId())) {
            throw new RuntimeException("Groupe introuvable pour la mise à jour.");
        }
        return groupeRepository.save(groupe);
    }

    @Override
    @Transactional

    public void deleteGroupe(Long id) {
        Groupe groupe = groupeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Groupe introuvable"));

        // 1. Supprimer les habilitations liées
        List<Habilitation> habilitations = habilitationRepository.findByGroupe(groupe);
        habilitationRepository.deleteAll(habilitations);

        // 2. Détacher les utilisateurs liés
//


        // 3. Supprimer le groupe
        groupeRepository.delete(groupe);
    }

    @Override
    @Transactional

    public Groupe getGroupeById(Long id) {
        return groupeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Groupe non trouvé avec l'ID : " + id));
    }
    @Override
    @Transactional
    public List<Groupe> getAllGroupes() {
        return groupeRepository.findAllWithHabilitations(); // ✅ simple, sans détour
    }

    @Override
    @Transactional

    public Groupe findByNom(String nom) {
        return (Groupe) groupeRepository.findByNom(nom)
                .orElseThrow(() -> new RuntimeException("Groupe non trouvé avec le nom : " + nom));
    }

    @Override
    public List<Groupe> findAllWithRessources() {
        return groupeRepository.findAllWithHabilitations();    }
}
