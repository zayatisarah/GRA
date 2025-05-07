package tn.esprit.usergra.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.usergra.entites.Ressource;
import tn.esprit.usergra.repositories.RessourceRepository;

import java.util.List;

@RestController
@RequestMapping("/ressource")
@RequiredArgsConstructor
public class RessourceController {
    private final RessourceRepository ressourceRepository;

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/all")
    public List<Ressource> getAll() {
        return ressourceRepository.findAll();
    }
}
