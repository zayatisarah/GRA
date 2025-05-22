package com.example.client.entites;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

    @ManyToOne
    @JoinColumn(name = "ID_ACTIONNAIRE", nullable = false)
    @JsonIgnoreProperties("portefeuilles")
    private Actionnaire actionnaire;

    @ManyToOne
    @JoinColumn(name = "ID_ACTION", nullable = false)
    @JsonIgnoreProperties("portefeuilles") // si Action a une liste aussi
    private Action action;

    @Column(name = "VALEUR_TOTALE", nullable = false)
    private Double valeurTotale;

    @Column(name = "QUANTITE", nullable = false)
    private Integer quantite;

    @Column(name = "DATECREATION", nullable = false)
    private LocalDateTime dateCreation;

    @Column(name = "USERCREATION", nullable = false)
    private String userCreation;

    @Column(name = "DATEMODIFICATION")
    private LocalDateTime dateModification;

    @Column(name = "USERMODIFICATION")
    private String userModification;
}
