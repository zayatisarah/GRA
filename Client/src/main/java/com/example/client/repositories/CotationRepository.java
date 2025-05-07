package com.example.client.repositories;

import com.example.client.entites.Cotation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CotationRepository extends JpaRepository<Cotation,Long> {
}
