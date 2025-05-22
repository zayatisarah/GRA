package com.example.client.repositories;

import com.example.client.entites.Action;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ActionRepository extends JpaRepository<Action , Long> {
    Optional<Action> findByIsin(String isin);
}
