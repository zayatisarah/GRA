package com.example.client.repositories;

import com.example.client.entites.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Long> {

    @Query("SELECT t FROM Transaction t " +
            "LEFT JOIN FETCH t.portefeuille p " +
            "LEFT JOIN FETCH p.action a " +
            "LEFT JOIN FETCH p.actionnaire an")
    List<Transaction> findAllWithPortefeuilleAndDetails();

    @Query("SELECT COUNT(t) > 0 FROM Transaction t WHERE t.dateTransaction = :date AND t.quantite = :quantite AND t.montants = :montants AND t.idType = :idType AND t.portefeuille.idPortefeuille = :idPortefeuille")
    boolean existsByAllFields(@Param("date") LocalDate date,
                              @Param("quantite") Long quantite,
                              @Param("montants") Double montants,
                              @Param("idType") Long idType,
                              @Param("idPortefeuille") Long idPortefeuille);

    boolean existsByDateTransactionAndQuantiteAndMontantsAndIdTypeAndIdPortefeuille(
            LocalDate dateTransaction,
            Long quantite,
            Double montants,
            Long idType,
            Long idPortefeuille
    );




}
