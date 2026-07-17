package niccolosciucco.u5_w3_d5.prenotazioni.repositories;

import niccolosciucco.u5_w3_d5.prenotazioni.entities.Prenotazione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PrenotazioneRepository extends JpaRepository<Prenotazione, UUID> {
}
