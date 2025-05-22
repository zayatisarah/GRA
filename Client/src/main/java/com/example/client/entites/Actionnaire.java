package com.example.client.entites;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Component
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Actionnaire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ACTIONNAIRE")
    private Long idActionnaire;

    @Column(unique = true) // Pour éviter les doublons
    private String matriculeActionnaire;


    @Column(name = "NOM_ACTIONNAIRE", nullable = false)
    private String nomActionnaire;

    @Column(name = "PRENOM_ACTIONNAIRE", nullable = true)
    private String prenomActionnaire;

    @Column(name = "EMAIL_ACTIONNAIRE", nullable = true)
    private String emailActionnaire;

    @Column(name = "TELEPHONE", nullable = false)
    private Long telephone;

    @Column(name = "DATE_CREATION")
    private LocalDateTime dateCreation;  // 🛠 Changer de String à LocalDateTime

    @Column(name = "USER_CREATION", nullable = false)
    private String userCreation;

    @Column(name = "DATE_MODIFICATION")
    private LocalDateTime dateModification;  // 🛠 Changer de String à LocalDateTime

    @Column(name = "USER_MODIFICATION", nullable = false)
    private String userModification;

    @OneToMany(mappedBy = "actionnaire")
    @JsonIgnoreProperties("actionnaire")

    private List<Portefeuille> portefeuilles;
    public Long getIdActionnaire() {
        return idActionnaire;
    }

    public void setIdActionnaire(Long idActionnaire) {
        this.idActionnaire = idActionnaire;
    }

    public String getNomActionnaire() {
        return nomActionnaire;
    }

    public void setNomActionnaire(String nomActionnaire) {
        this.nomActionnaire = nomActionnaire;
    }

    public String getMatriculeActionnaire() {
        return matriculeActionnaire;
    }

    public void setMatriculeActionnaire(String matriculeActionnaire) {
        this.matriculeActionnaire = matriculeActionnaire;
    }

    public String getPrenomActionnaire() {
        return prenomActionnaire;
    }

    public void setPrenomActionnaire(String prenomActionnaire) {
        this.prenomActionnaire = prenomActionnaire;
    }

    public String getEmailActionnaire() {
        return emailActionnaire;
    }

    public void setEmailActionnaire(String emailActionnaire) {
        this.emailActionnaire = emailActionnaire;
    }

    public Long getTelephone() {
        return telephone;
    }

    public void setTelephone(Long telephone) {
        this.telephone = telephone;
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

    public void setMatricule(String numeroActionnaire) {
    }
}


