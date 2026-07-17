package niccolosciucco.u5_w3_d5.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import niccolosciucco.u5_w3_d5.exceptions.Custom.Unauthorized;
import niccolosciucco.u5_w3_d5.security.tools.JwtTools;
import niccolosciucco.u5_w3_d5.utenti.entities.Utente;
import niccolosciucco.u5_w3_d5.utenti.services.UtenteService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class TokenFilter extends OncePerRequestFilter {
    private final JwtTools jwtTools;
    private final UtenteService utenteService;

    public TokenFilter(JwtTools jwtTools, UtenteService utenteService) {
        this.jwtTools = jwtTools;
        this.utenteService = utenteService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer "))
            throw new Unauthorized("Impossibile completare l'operazione: inserire il token nella richiesta");
        String token = header.replace("Bearer ", "");
        this.jwtTools.verify(token);
        UUID utenteId = this.jwtTools.idFromToken(token);
        Utente utente = this.utenteService.findById(utenteId);
        Authentication authentication = new UsernamePasswordAuthenticationToken(utente, null, utente.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        if (request.getServletPath().startsWith("/auth")) {
            return true;
        }

        if (request.getServletPath().startsWith("/eventi") && request.getMethod().equalsIgnoreCase("GET")) {
            return true;
        }

        if (request.getServletPath().startsWith("/utenti") && request.getMethod().equalsIgnoreCase("POST")) {
            return true;
        }

        return false;
    }
}
