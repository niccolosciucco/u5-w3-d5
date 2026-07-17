package niccolosciucco.u5_w3_d5.utenti.services;

import niccolosciucco.u5_w3_d5.eventi.repositories.EventoRepository;
import niccolosciucco.u5_w3_d5.exceptions.Custom.AlreadyInDb;
import niccolosciucco.u5_w3_d5.exceptions.Custom.BadRequest;
import niccolosciucco.u5_w3_d5.exceptions.Custom.NotFound;
import niccolosciucco.u5_w3_d5.prenotazioni.repositories.PrenotazioneRepository;
import niccolosciucco.u5_w3_d5.utenti.DTO.UtenteDTO;
import niccolosciucco.u5_w3_d5.utenti.entities.Utente;
import niccolosciucco.u5_w3_d5.utenti.repositories.UtenteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UtenteService {
    private final UtenteRepository utenteRepository;
    private final EventoRepository eventoRepository;
    private final PrenotazioneRepository prenotazioneRepository;

    public UtenteService(UtenteRepository utenteRepository,
                         EventoRepository eventoRepository,
                         PrenotazioneRepository prenotazioneRepository) {
        this.utenteRepository = utenteRepository;
        this.eventoRepository = eventoRepository;
        this.prenotazioneRepository = prenotazioneRepository;
    }

    public Page<Utente> getAll(int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return this.utenteRepository.findAll(pageable);
    }

    public Utente findById(UUID id) {
        return this.utenteRepository.findById(id)
                .orElseThrow(() -> new NotFound("Utente con ID " + id + " non trovato"));
    }

    public Utente post(UtenteDTO dto) {
        if (this.utenteRepository.existsByUsername(dto.username())) {
            throw new AlreadyInDb("Lo username '" + dto.username() + "' è già in uso");
        }

        Utente nuovoUtente = new Utente(dto.username(), dto.password(), dto.ruolo());

        return this.utenteRepository.save(nuovoUtente);
    }

    public Utente put(UUID id, UtenteDTO dto) {
        Utente found = this.findById(id);
        if (!found.getUsername().equals(dto.username()) && this.utenteRepository.existsByUsername(dto.username())) {
            throw new AlreadyInDb("Lo username '" + dto.username() + "' è già in uso");
        }

        boolean isChanged = false;

        if (!found.getUsername().equals(dto.username())) {
            found.setUsername(dto.username());
            isChanged = true;
        }

        if (!found.getPassword().equals(dto.password())) {
            found.setPassword(dto.password());
            isChanged = true;
        }

        if (found.getRuolo() != dto.ruolo()) {
            found.setRuolo(dto.ruolo());
            isChanged = true;
        }

        if (isChanged) {
            return this.utenteRepository.save(found);
        }

        return found;
    }

    public void delete(UUID id) {
        Utente found = this.findById(id);
        if (this.prenotazioneRepository.existsByUtente(found)) {
            throw new BadRequest("Impossibile eliminare l'utente: ha ancora prenotazioni attive.");
        }

        if (this.eventoRepository.existsByOrganizzatore(found)) {
            throw new BadRequest("Impossibile eliminare l'utente: è l'organizzatore di alcuni eventi attivi.");
        }

        this.utenteRepository.delete(found);
    }
}