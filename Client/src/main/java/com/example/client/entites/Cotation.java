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
public class Cotation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCotation;

    @Column(nullable = false)
    private Double prix;

    @Column(name = "DATECOTATION", nullable = false)
    private LocalDate dateCotation;


    @ManyToOne
    @JoinColumn(name = "IDACTIONS", nullable = false) // 🔁 nom exact de ta colonne Oracle
    private Action action;
}
