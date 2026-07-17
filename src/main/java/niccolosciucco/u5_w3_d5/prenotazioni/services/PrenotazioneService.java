package niccolosciucco.u5_w3_d5.prenotazioni.services;

import niccolosciucco.u5_w3_d5.eventi.entities.Evento;
import niccolosciucco.u5_w3_d5.eventi.repositories.EventoRepository;
import niccolosciucco.u5_w3_d5.exceptions.Custom.AlreadyInDb;
import niccolosciucco.u5_w3_d5.exceptions.Custom.BadRequest;
import niccolosciucco.u5_w3_d5.exceptions.Custom.NotFound;
import niccolosciucco.u5_w3_d5.prenotazioni.DTO.PrenotazioneDTO;
import niccolosciucco.u5_w3_d5.prenotazioni.entities.Prenotazione;
import niccolosciucco.u5_w3_d5.prenotazioni.repositories.PrenotazioneRepository;
import niccolosciucco.u5_w3_d5.utenti.entities.Utente;
import niccolosciucco.u5_w3_d5.utenti.services.UtenteService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PrenotazioneService {

    private final PrenotazioneRepository prenotazioneRepository;
    private final UtenteService utenteService;
    private final EventoRepository eventoRepository; // Iniettato direttamente per aggiornare i posti dell'evento

    public PrenotazioneService(PrenotazioneRepository prenotazioneRepository,
                               UtenteService utenteService,
                               EventoRepository eventoRepository) {
        this.prenotazioneRepository = prenotazioneRepository;
        this.utenteService = utenteService;
        this.eventoRepository = eventoRepository;
    }

    public Prenotazione findById(UUID id) {
        return this.prenotazioneRepository.findById(id)
                .orElseThrow(() -> new NotFound("Prenotazione con ID " + id + " non trovata"));
    }

    public Prenotazione post(PrenotazioneDTO dto) {
        Utente utente = this.utenteService.findById(dto.utenteId());

        Evento evento = this.eventoRepository.findById(dto.eventoId())
                .orElseThrow(() -> new NotFound("Evento non trovato con ID " + dto.eventoId()));

        if (evento.getPostiDisponibili() <= 0) {
            throw new BadRequest("I posti per l'evento '" + evento.getTitolo() + "' sono esauriti!");
        }

        if (this.prenotazioneRepository.existsByUtenteAndEvento(utente, evento)) {
            throw new AlreadyInDb("Hai già una prenotazione attiva per questo evento!");
        }

        Prenotazione prenotazione = new Prenotazione(utente, evento, LocalDateTime.now());

        evento.setPostiDisponibili(evento.getPostiDisponibili() - 1);
        this.eventoRepository.save(evento);

        return this.prenotazioneRepository.save(prenotazione);
    }

    public void delete(UUID prenotazioneId) {
        Prenotazione prenotazione = this.findById(prenotazioneId);
        Evento evento = prenotazione.getEvento();

        evento.setPostiDisponibili(evento.getPostiDisponibili() + 1);
        this.eventoRepository.save(evento);

        this.prenotazioneRepository.delete(prenotazione);
    }
}
