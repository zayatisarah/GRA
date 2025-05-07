package com.example.client.repositories;

import com.example.client.entites.Actionnaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActionnaireRepository extends JpaRepository<Actionnaire ,Long> {
}
