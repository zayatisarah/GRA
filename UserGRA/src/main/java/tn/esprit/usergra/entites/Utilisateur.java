package tn.esprit.usergra.entites;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import tn.esprit.usergra.entites.enumr.Role;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity  // ✅ Indique que c'est une entité JPA
@Table(name = "utilisateur") // ✅ Nom de la table en base de données
public class Utilisateur implements UserDetails {  // ✅ Implémente UserDetails

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ✅ Clé primaire auto-générée
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;  // Nom d'utilisateur ou email

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)  // 🔥 Assure que le rôle est stocké sous forme de String
    private Role role;

    @ManyToOne
    @JsonIgnoreProperties({"utilisateurs", "autresChampsSiBoucle"})
    @JoinColumn(name = "groupe_id")
    private Groupe groupe;

    @Column(nullable = false, unique = true)
    private String matricule;

    @Column(nullable = false)
    private boolean actif = true;
    @ManyToMany

    @JoinTable(
            name = "utilisateur_droit",
            joinColumns = @JoinColumn(name = "utilisateur_id"),
            inverseJoinColumns = @JoinColumn(name = "droit_id")
    )

    private List<Droit> droits;
    // ✅ Constructeurs
@JsonIgnore
    @Column(name = "first_login")
    private boolean firstLogin = true;

    public Utilisateur() {
    }

    public Utilisateur(String username, String email, String password, Role role,String matricule) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.matricule=matricule;

    }

    // ✅ Implémentation des méthodes de UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.role.name()));
    }


    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // ✅ Getters et Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public void setGroupe(Groupe groupe) {
        this.groupe = groupe;
    }

    public void setEnabled(boolean enabled) {
    }


    public Groupe getGroupe() {
        return groupe;
    }


    public void serMatricule(String adm001) {
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }


    public boolean isActif() {
        return this.actif;
    }
    @JsonProperty("firstLogin")
    public boolean isFirstLogin() {
        return firstLogin;
    }


    @Transient
    private List<Long> droitIds;

    public List<Long> getDroitIds() {
        return droitIds;
    }

    public void setDroitIds(List<Long> droitIds) {
        this.droitIds = droitIds;
    }


}


