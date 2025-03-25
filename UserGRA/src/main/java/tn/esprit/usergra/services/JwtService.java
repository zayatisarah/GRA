package tn.esprit.usergra.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Service
public class JwtService {

    // Clé secrète générée de manière sécurisée (doit être d'au moins 256 bits pour HMAC SHA)
    private static final String SECRET_KEY = "8a$3JdK!zPqT&vXb@9LwY#RmN*CtZ7Fg5QKdVpXr8a$3JdK!zPqT&vXb@9LwY#RmN*CtZ7Fg5QKdVpXr";

    // Conversion de la clé secrète en clé utilisable par JWT
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    // ✅ Méthode pour valider le token
    public boolean validateToken(String token) {
        try {
            System.out.println("🔍 Token reçu pour validation : " + token);

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)  // Vérifie bien que `SECRET_KEY` est correcte
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            System.out.println("✅ Claims du token : " + claims);

            return claims != null;
        } catch (Exception e) {
            System.out.println("❌ Erreur lors de la validation du token : " + e.getMessage());
            return false;
        }
    }


    // ✅ Méthode pour extraire le sujet (ex: username)
    public String extractSubject(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // ✅ Méthode pour extraire les rôles
    public List<String> extractRoles(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("roles", List.class); // Assurez-vous que les rôles sont bien stockés sous "roles"
    }

    // ✅ Méthode générique pour extraire une information du token
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // ✅ Méthode pour extraire toutes les informations du token
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
