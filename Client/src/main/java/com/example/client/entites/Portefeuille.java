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
public class Portefeuille {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDPORTFEUILLE", nullable = false)
    private Long idPortefeuille;

    @Column(name = "IDACTIONNAIRE", nullable = false)
    private Long idActionnaire;

    // @ManyToOne(fetch = FetchType.LAZY)
    //  @JoinColumn(name = "IDACTIONS", nullable = false)
    // private Actions idActions;

    @Column(name = "VALEUR_TOTALE", nullable = false)
    private Double valeurTotale;

    @Column(name = "QUANTITE", nullable = false)
    private Integer quantite;

    @Column(name = "DATECREATION", nullable = false)
    private LocalDate dateCreation;


    @Column(name = "USERCREATION", nullable = false)
    private String userCreation;

    @Column(name = "DATEMODIFICATION", nullable = true)
    private LocalDate dateModification;

    @Column(name = "USERMODIFICATION", nullable = true)
    private String userModification;
    @ManyToOne
    @JoinColumn(name = "idActions")
    private Action action;
    @ManyToOne
    @JoinColumn(name = "ID_ACTIONNAIRE") // ou la vraie colonne si elle a un nom différent
    private Actionnaire actionnaire;


}

