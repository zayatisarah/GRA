package com.example.client.repositories;

import com.example.client.entites.Portefeuille;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PortefeuilleRepository extends JpaRepository<Portefeuille,Long> {
}
