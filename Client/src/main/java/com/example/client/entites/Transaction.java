package com.example.client.entites;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    private Long idPortefeuille; // clé technique

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDPORTEFEUILLE", insertable = false, updatable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "transactions"})
    private Portefeuille portefeuille; // lien vers objet

    @Column(name = "MONTANTS", nullable = false)
    private Double montants;

    @Column(name = "DATETRANSACTION", nullable = false)
    private LocalDate dateTransaction;

    @Column(name = "IDTYPE", nullable = false)
    private Long idType;

    @Column(name = "QUANTITE", nullable = false)
    private Long quantite;

    public void setIdTransaction(String idTransaction) {
        // peut être ignoré si inutile
    }
}
