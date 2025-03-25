package tn.esprit.usergra.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.stereotype.Component;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import tn.esprit.usergra.entites.JwtUtil;
import java.io.IOException;

@Component
public class JwtAuthentificationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public JwtAuthentificationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String path = request.getServletPath();
        System.out.println("🚀 [JWT Filter] Vérification du token pour : " + path);

        // ✅ Ignorer Swagger et l'authentification
        if (path.startsWith("/auth/") || path.startsWith("/swagger-ui") || path.startsWith("/v3/api-docs")) {
            System.out.println("✅ [JWT Filter] Ignoré pour " + path);
            chain.doFilter(request, response);
            return;
        }

        // 🔑 Vérification du token JWT
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("⚠️ [JWT Filter] Aucun token trouvé pour " + path);
            chain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        System.out.println("🔑 [JWT Filter] Token extrait : " + token);

        String username = jwtUtil.extractUsername(token);
        System.out.println("🔍 [JWT Filter] Username extrait du token : " + username);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            System.out.println("👤 [JWT Filter] UserDetails récupéré : " + userDetails.getUsername());

            if (jwtUtil.validateToken(token, userDetails)) {
                System.out.println("✅ [JWT Filter] Token valide !");
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else {
                System.out.println("❌ [JWT Filter] Token invalide !");
            }
        } else {
            System.out.println("⚠️ [JWT Filter] Username est null ou déjà authentifié !");
        }

        chain.doFilter(request, response);
    }

}