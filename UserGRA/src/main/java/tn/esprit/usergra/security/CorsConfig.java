package tn.esprit.usergra.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration configuration = new CorsConfiguration();

        // ✅ Définir les origines autorisées (PAS "*")
        configuration.setAllowedOriginPatterns(List.of("http://localhost:4200", "http://127.0.0.1:8050"));

        // ✅ Méthodes HTTP autorisées
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // ✅ Headers autorisés
        configuration.setAllowedHeaders(List.of("*"));

        // ✅ Headers exposés (ex: Authorization pour JWT)
        configuration.setExposedHeaders(List.of("Authorization"));

        // ✅ Activer les credentials (ex: cookies sécurisés)
        configuration.setAllowCredentials(true);

        source.registerCorsConfiguration("/**", configuration);
        return new CorsFilter(source);
    }
}
