package com.example.client.repositories;

import com.example.client.entites.Action;
import com.example.client.entites.Actionnaire;
import com.example.client.entites.Portefeuille;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PortefeuilleRepository extends JpaRepository<Portefeuille,Long> {
    Optional<Portefeuille> findByActionnaireAndAction(Actionnaire actionnaire, Action action);
}
