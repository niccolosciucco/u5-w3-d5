package niccolosciucco.u5_w3_d5.utenti.repositories;

import niccolosciucco.u5_w3_d5.utenti.entities.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UtenteRepository extends JpaRepository<Utente, UUID> {
}
