package com.example.client.services;

import com.example.client.entites.Actionnaire;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ActionnaireService {
    Actionnaire addActionnaire(Actionnaire actionnaire);
    Actionnaire deleteActionnaire(long id);
    List<Actionnaire> getAllActionnaires();
    Actionnaire updateActionnaire(Actionnaire actionnaire);
}
