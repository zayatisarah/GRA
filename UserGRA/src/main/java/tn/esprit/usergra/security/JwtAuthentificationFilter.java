package tn.esprit.usergra.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;
import tn.esprit.usergra.services.JwtService;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthentificationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthentificationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String path = request.getRequestURI();
        System.out.println("🔍 Filtre JWT déclenché pour : " + path);

        final String authHeader = request.getHeader("Authorization");
        System.out.println("🕵️ Header Authorization : " + authHeader);

        if (path.contains("/auth/")) {
            System.out.println("🔓 Chemin public (auth), pas besoin de token.");
            filterChain.doFilter(request, response);
            return;
        }

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("🚫 Aucun token ou mauvais format, on continue sans authentification.");
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7);
        final String username = jwtService.extractUsername(jwt);
        System.out.println("🧩 Token détecté : " + jwt);
        System.out.println("👤 Matricule extrait : " + username);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (jwtService.isTokenValid(jwt, userDetails)) {
                List<String> authoritiesList = jwtService.extractRoles(jwt);
                System.out.println("🛡️ Authorities extraites du token : " + authoritiesList);

                List<SimpleGrantedAuthority> authorities = authoritiesList.stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList();

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        authorities
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                System.out.println("🔐 Comparaison token vs userDetails : "
                        + username + " == " + userDetails.getUsername());
                System.out.println("🛡️ Rôles injectés : " + authorities);

                SecurityContextHolder.getContext().setAuthentication(authToken);
                System.out.println("🎯 Authorities dans SecurityContext : " +
                        SecurityContextHolder.getContext().getAuthentication().getAuthorities());
                System.out.println("🎯 CONTEXTE AUTH FINAL : " + SecurityContextHolder.getContext().getAuthentication());


                System.out.println("✅ Utilisateur authentifié : " + userDetails.getUsername());
            } else {
                System.out.println("❌ Token invalide !");
            }
        }

        // 🔥 TRÈS IMPORTANT 🔥
        filterChain.doFilter(request, response);
    }
}
