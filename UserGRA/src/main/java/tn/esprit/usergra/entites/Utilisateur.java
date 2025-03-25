package tn.esprit.usergra.entites;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import tn.esprit.usergra.entites.enumr.Role;

import java.util.Collection;
import java.util.Collections;

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


    // ✅ Constructeurs
    public Utilisateur() {}

    public Utilisateur(String username, String email, String password, Role role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // ✅ Implémentation des méthodes de UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(() -> role.name()); // ✅ Retourne le rôle comme une autorité
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
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public void setPassword(String password) { this.password = password; }
    public void setUsername(String username) { this.username = username; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
}
