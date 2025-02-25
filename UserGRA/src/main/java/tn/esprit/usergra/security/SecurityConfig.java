package tn.esprit.usergra.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import tn.esprit.usergra.entites.JwtUtil;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtUtil jwtUtil, JwtAuthentificationFilter jwtFilter) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/user/add").hasAuthority("ROLE_ADMIN") // ✅ Solution
                        .requestMatchers("/user/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_RESPONSABLE")
                        .anyRequest().authenticated()
                );

        // 🔥 Ajout du filtre JWT en tant que bean
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public JwtAuthentificationFilter jwtAuthenticationFilter(JwtUtil jwtUtil) {
        return new JwtAuthentificationFilter(jwtUtil);
    }
}
