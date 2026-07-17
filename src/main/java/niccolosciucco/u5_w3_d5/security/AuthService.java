package niccolosciucco.u5_w3_d5.security;

import niccolosciucco.u5_w3_d5.exceptions.Custom.Unauthorized;
import niccolosciucco.u5_w3_d5.security.DTO.LoginDTO;
import niccolosciucco.u5_w3_d5.security.tools.JwtTools;
import niccolosciucco.u5_w3_d5.utenti.entities.Utente;
import niccolosciucco.u5_w3_d5.utenti.services.UtenteService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UtenteService utenteService;
    private final JwtTools jwtTools;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UtenteService utenteService, JwtTools jwtTools, PasswordEncoder passwordEncoder) {
        this.utenteService = utenteService;
        this.jwtTools = jwtTools;
        this.passwordEncoder = passwordEncoder;
    }

    public String check(LoginDTO loginDTO) {
        Utente utente = this.utenteService.findByUsername(loginDTO.username());
        if (this.passwordEncoder.matches(loginDTO.password(), utente.getPassword())) {
            return this.jwtTools.generateToken(utente);
        } else {
            throw new Unauthorized("Credenziali non valide.");
        }
    }
}