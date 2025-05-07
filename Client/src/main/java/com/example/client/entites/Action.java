package com.example.client.entites;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Action {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDACTIONS")
    private Long idActions;

    @Column(name = "NOMSOCIETE", nullable = false)
    private String nomSociete;

    @Column(name = "ENVENTE", nullable = false)
    private Boolean enVente;

    @Column(name = "PRIX", nullable = false)
    private Double prix;

    @Column(name = "SECTEUR", nullable = false)
    private String secteur;

    @Column(name = "DATECREATION")
    private LocalDateTime dateCreation;

    @Column(name = "USERCREATION", nullable = false)
    private String userCreation;

    @Column(name = "DATEMODIFICATION")
    private LocalDateTime dateModification;

    @Column(name = "USERMODIFICATION", nullable = false)
    private String userModification;

    @Column(name = "DEVICE", nullable = false)
    private String device;
    @Column(name = "ISIN", unique = true, nullable = false)
    private String isin;

    @OneToMany(mappedBy = "action", cascade = CascadeType.ALL)
    @JsonIgnore //
    private List<Portefeuille> portefeuilles;

    @OneToMany(mappedBy = "action", cascade = CascadeType.ALL)
    @JsonIgnore // ✅
    private List<Cotation> cotations;

    public Long getIdActions() {
        return idActions;
    }

    public void setIdActions(Long idActions) {
        this.idActions = idActions;
    }

    public String getNomSociete() {
        return nomSociete;
    }

    public void setNomSociete(String nomSociete) {
        this.nomSociete = nomSociete;
    }

    public Boolean getEnVente() {
        return enVente;
    }

    public void setEnVente(Boolean enVente) {
        this.enVente = enVente;
    }

    public Double getPrix() {
        return prix;
    }

    public void setPrix(Double prix) {
        this.prix = prix;
    }

    public String getSecteur() {
        return secteur;
    }

    public void setSecteur(String secteur) {
        this.secteur = secteur;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getUserCreation() {
        return userCreation;
    }

    public void setUserCreation(String userCreation) {
        this.userCreation = userCreation;
    }

    public LocalDateTime getDateModification() {
        return dateModification;
    }

    public void setDateModification(LocalDateTime dateModification) {
        this.dateModification = dateModification;
    }

    public String getUserModification() {
        return userModification;
    }

    public void setUserModification(String userModification) {
        this.userModification = userModification;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }
}

