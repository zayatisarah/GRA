package tn.esprit.usergra.services;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import tn.esprit.usergra.entites.Utilisateur;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    // 🔐 Génération du token
    public String generateToken(Utilisateur user) {
        Map<String, Object> extraClaims = Map.of(
                "authorities", List.of(user.getRole().name()),
                "firstLogin", user.isFirstLogin(),
                "matricule", user.getMatricule()
        );

        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(user.getMatricule())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10h
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }



    // ✅ Extraction du "subject" (le matricule)
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);  // ⚠️ doit renvoyer le matricule
    }


    // ✅ Extraction des rôles (optionnel)
    public List<String> extractRoles(String token) {
        try {
            Claims claims = extractAllClaims(token);
            Object rawAuthorities = claims.get("authorities");

            if (rawAuthorities instanceof List<?>) {
                return ((List<?>) rawAuthorities).stream()
                        .map(Object::toString)
                        .toList();
            } else {
                return List.of();
            }
        } catch (Exception e) {
            System.out.println("❌ Erreur lors de l'extraction des rôles : " + e.getMessage());
            return List.of();
        }
    }




    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // ✅ Extraction brute de tous les claims
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // ✅ Vérification expiration
    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    // ✅ Validation du token (utilisé dans le filtre JWT)
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }



    public boolean isTokenValid(String jwt, UserDetails userDetails) {
        final String username = extractUsername(jwt); // récupère le matricule du token
        boolean expired = isTokenExpired(jwt);
        boolean valid = username.equals(userDetails.getUsername()) && !expired;

        System.out.println("🔑 Vérification token pour utilisateur : " + userDetails.getUsername());
        System.out.println("🔍 Sujet extrait du token : " + username);
        System.out.println("📅 Token expiré ? : " + expired);
        System.out.println("✅ Token valide ? : " + valid);

        return valid;
    }

}
