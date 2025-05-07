package tn.esprit.usergra.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.LockedException;
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
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(httpBasic -> httpBasic.disable())
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> {
                            if (authException instanceof LockedException) {
                                System.out.println("🔒 Tentative connexion bloquée !");
                                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Compte bloqué !");
                            } else {
                                System.out.println("⚠️ [Security] Rejeté avec 401 - Authentification requise !");
                                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                            }
                        })
                )
                .authorizeHttpRequests(auth -> auth
                        // Autorisations publiques
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")

                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/webjars/**").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Accès selon rôles
                        .requestMatchers("/user/update-password").authenticated()
                        .requestMatchers("/user/all").permitAll()
                        .requestMatchers("/admin/reset-password/**").hasAuthority("ROLE_ADMIN")

                        .requestMatchers("/user/add").permitAll() // pour la création initiale
                        .requestMatchers("/user/delete/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/user/update").hasAnyAuthority("ROLE_ADMIN", "ROLE_RESPONSABLE")
                        .requestMatchers("/user/toggle-block/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/user/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_RESPONSABLE")


                        .requestMatchers(HttpMethod.POST, "/groupe/add").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/groupe/update").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/groupe/delete/**").hasAuthority("ROLE_ADMIN")


                        .anyRequest().authenticated()
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
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
