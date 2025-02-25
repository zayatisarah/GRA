package tn.esprit.usergra.entites;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JwtUtil {

    private SecretKey secretKey;
    private final String KEY_FILE_PATH = "src/main/resources/secret.key";
    private static final long EXPIRATION_TIME = 86400000; // 1 jour en ms

    @PostConstruct
    public void init() {
        try {
            Path path = Path.of(KEY_FILE_PATH);
            if (Files.exists(path)) {
                byte[] keyBytes = Files.readAllBytes(path);
                this.secretKey = new SecretKeySpec(keyBytes, "HmacSHA256");
            } else {
                byte[] keyBytes = new byte[32];
                new java.security.SecureRandom().nextBytes(keyBytes);
                Files.write(path, keyBytes, StandardOpenOption.CREATE);
                this.secretKey = new SecretKeySpec(keyBytes, "HmacSHA256");
            }
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors du chargement/génération de la clé secrète", e);
        }
    }

    public String generateToken(Utilisateur user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole()); // ✅ Ajout du rôle dans le token

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(secretKey, SignatureAlgorithm.HS256) // ✅ Correction
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public String extractRole(String token) {
        return extractClaims(token).get("role", String.class); // ✅ Récupère le rôle
    }

    public List<String> extractRoles(String token) {
        Claims claims = extractClaims(token);
        return claims.get("roles", List.class);
    }

    private Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token, String email) {
        try {
            String extractedEmail = extractUsername(token);
            return (extractedEmail.equals(email) && !isTokenExpired(token));
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        Date expiration = extractClaims(token).getExpiration();
        return expiration.before(new Date());
    }
}
