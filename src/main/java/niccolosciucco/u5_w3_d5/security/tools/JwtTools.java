package niccolosciucco.u5_w3_d5.security.tools;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import niccolosciucco.u5_w3_d5.exceptions.Custom.Unauthorized;
import niccolosciucco.u5_w3_d5.utenti.entities.Utente;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
public class JwtTools {
    @Value("${tkn.pass}")
    private String secret;

    public String generateToken(Utente utente) {
        return Jwts.builder()
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7))
                .subject(String.valueOf(utente.getId()))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();
    }

    public void verify(String token) {
        try {
            Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secret.getBytes())).build().parse(token);
        } catch (Exception ex) {
            throw new Unauthorized("Errore nella verifica del token");
        }
    }

    public UUID idFromToken(String token) {
        return UUID.fromString(Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secret.getBytes())).build().parseSignedClaims(token).getPayload().getSubject());
    }
}
