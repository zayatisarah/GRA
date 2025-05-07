package com.example.client.services.imp;

import com.example.client.entites.Actionnaire;
import com.example.client.repositories.ActionnaireRepository;
import com.example.client.services.ActionnaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class ActionnaireServiceImp implements ActionnaireService {
    private final ActionnaireRepository actionnaireRepository;
    @Autowired
    public ActionnaireServiceImp(ActionnaireRepository actionnaireRepository) {this.actionnaireRepository=actionnaireRepository;}
    @Override
    public Actionnaire addActionnaire(Actionnaire actionnaire) {
        // Vérifier si dateCreation et dateModification sont nulles et les définir à l'heure actuelle
        LocalDateTime now = LocalDateTime.now();
        if (actionnaire.getDateCreation() == null) {
            actionnaire.setDateCreation(now);
        }
        if (actionnaire.getDateModification() == null) {
            actionnaire.setDateModification(now);
        }

        // Vérification des champs obligatoires
        if (actionnaire.getNomActionnaire() == null || actionnaire.getPrenomActionnaire() == null ||
                actionnaire.getEmailActionnaire() == null || actionnaire.getTelephone() == null ||
                actionnaire.getUserCreation() == null || actionnaire.getUserModification() == null) {
            throw new IllegalArgumentException("Tous les champs obligatoires doivent être renseignés");
        }

        // Sauvegarder l'actionnaire dans la base de données
        return actionnaireRepository.save(actionnaire);
    }


    @Override
    public Actionnaire deleteActionnaire(long id) {
        actionnaireRepository.deleteById(id);
        return null;
    }

    @Override
    public List<Actionnaire> getAllActionnaires() {
        return actionnaireRepository.findAll();
    }

    @Override
    public Actionnaire updateActionnaire(Actionnaire actionnaire) {
        return actionnaireRepository.save(actionnaire);
    }
}
