package niccolosciucco.u5_w3_d5.eventi.repositories;

import niccolosciucco.u5_w3_d5.eventi.entities.Evento;
import niccolosciucco.u5_w3_d5.utenti.entities.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EventoRepository extends JpaRepository<Evento, UUID> {
    boolean existsByOrganizzatore(Utente organizzatore);
}
