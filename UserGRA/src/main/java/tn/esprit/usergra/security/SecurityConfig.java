package tn.esprit.usergra.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    private final JwtAuthentificationFilter jwtFilter;

    public SecurityConfig(JwtAuthentificationFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable()) // ✅ Désactiver CSRF (nécessaire pour les APIs REST)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // ✅ API Stateless
                .httpBasic(httpBasic -> httpBasic.disable()) // ✅ Désactive Basic Auth

                .exceptionHandling(exception -> exception.authenticationEntryPoint((request, response, authException) -> {
                    System.out.println("⚠️ [Security] Rejeté avec 403 - Authentification requise !");
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                }))

                .authorizeHttpRequests(auth -> auth
                        // ✅ Autoriser l'authentification sans token
                        .requestMatchers("/auth/**").permitAll()

                        // ✅ Autoriser Swagger UI et API Docs
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/swagger-ui/index.html",
                                "/v3/api-docs/**",
                                "/swagger-resources/**",
                                "/webjars/**"
                        ).permitAll()

                        // ✅ Autoriser les requêtes OPTIONS (CORS)
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // 🔐 Protéger toutes les autres routes
                        .requestMatchers("/user/add").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/user/delete/**").permitAll()
                        .requestMatchers("/user/update/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_RESPONSABLE")

                        .requestMatchers("/user/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_RESPONSABLE")

                        .anyRequest().authenticated() // 🔒 Tout le reste nécessite une authentification
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // ✅ Utilisation de allowedOriginPatterns au lieu de allowedOrigins
        configuration.setAllowedOriginPatterns(List.of("*"));

        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));

        // ✅ Autorisation des credentials avec une configuration correcte
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }


}
