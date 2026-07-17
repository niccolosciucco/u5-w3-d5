package niccolosciucco.u5_w3_d5.prenotazioni.services;

import niccolosciucco.u5_w3_d5.eventi.entities.Evento;
import niccolosciucco.u5_w3_d5.eventi.repositories.EventoRepository;
import niccolosciucco.u5_w3_d5.exceptions.Custom.AlreadyInDb;
import niccolosciucco.u5_w3_d5.exceptions.Custom.BadRequest;
import niccolosciucco.u5_w3_d5.exceptions.Custom.NotFound;
import niccolosciucco.u5_w3_d5.exceptions.Custom.Unauthorized;
import niccolosciucco.u5_w3_d5.prenotazioni.DTO.PrenotazioneDTO;
import niccolosciucco.u5_w3_d5.prenotazioni.entities.Prenotazione;
import niccolosciucco.u5_w3_d5.prenotazioni.repositories.PrenotazioneRepository;
import niccolosciucco.u5_w3_d5.utenti.entities.Utente;
import niccolosciucco.u5_w3_d5.utenti.enums.RuoloUtente;
import niccolosciucco.u5_w3_d5.utenti.services.UtenteService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PrenotazioneService {

    private final PrenotazioneRepository prenotazioneRepository;
    private final UtenteService utenteService;
    private final EventoRepository eventoRepository;

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

    public List<Prenotazione> findByUtente(Utente utente) {
        return this.prenotazioneRepository.findByUtente(utente);
    }

    public Prenotazione post(PrenotazioneDTO dto, Utente utenteLoggato) {
        Evento evento = this.eventoRepository.findById(dto.eventoId())
                .orElseThrow(() -> new NotFound("Evento non trovato con ID " + dto.eventoId()));

        if (evento.getPostiDisponibili() <= 0) {
            throw new BadRequest("I posti per l'evento '" + evento.getTitolo() + "' sono esauriti!");
        }

        if (this.prenotazioneRepository.existsByUtenteAndEvento(utenteLoggato, evento)) {
            throw new AlreadyInDb("Hai già una prenotazione attiva per questo evento!");
        }

        Prenotazione prenotazione = new Prenotazione(utenteLoggato, evento, LocalDateTime.now());

        evento.setPostiDisponibili(evento.getPostiDisponibili() - 1);
        this.eventoRepository.save(evento);

        return this.prenotazioneRepository.save(prenotazione);
    }

    public void delete(UUID prenotazioneId, Utente utenteLoggato) {
        Prenotazione prenotazione = this.findById(prenotazioneId);
        boolean isOwner = prenotazione.getUtente().getId().equals(utenteLoggato.getId());
        boolean isOrganizzatore = utenteLoggato.getRuolo() == RuoloUtente.ORGANIZZATORE;

        if (!isOwner && !isOrganizzatore) {
            throw new Unauthorized("Non sei autorizzato a cancellare questa prenotazione");
        }

        Evento evento = prenotazione.getEvento();

        evento.setPostiDisponibili(evento.getPostiDisponibili() + 1);
        this.eventoRepository.save(evento);

        this.prenotazioneRepository.delete(prenotazione);
    }
}
