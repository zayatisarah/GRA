package com.example.client.repositories;

import com.example.client.entites.Actionnaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ActionnaireRepository extends JpaRepository<Actionnaire ,Long> {
    Optional<Actionnaire> findByMatriculeActionnaire(String matriculeActionnaire);
}
