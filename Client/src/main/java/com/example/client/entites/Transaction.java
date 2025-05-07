package com.example.client.entites;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_seq")
    @SequenceGenerator(name = "transaction_seq", sequenceName = "TRANSACTION_SEQ", allocationSize = 1)
    @Column(name = "IDTRANSACTIONS", nullable = false)
    private Long idTransactions;

    @Column(name = "IDPORTEFEUILLE", nullable = false)
    private Long idPortefeuille;

    @Column(name = "MONTANTS", nullable = false)
    private Double montants;

    @Column(name = "DATETRANSACTION", nullable = false)
    private LocalDate dateTransaction;


    @Column(name = "IDTYPE", nullable = false)
    private Long idType;

    @Column(name = "QUANTITE", nullable = false)
    private Long quantite;




}
