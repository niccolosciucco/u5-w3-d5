package niccolosciucco.u5_w3_d5.eventi.services;

import niccolosciucco.u5_w3_d5.eventi.DTO.EventoDTO;
import niccolosciucco.u5_w3_d5.eventi.entities.Evento;
import niccolosciucco.u5_w3_d5.eventi.repositories.EventoRepository;
import niccolosciucco.u5_w3_d5.exceptions.Custom.BadRequest;
import niccolosciucco.u5_w3_d5.exceptions.Custom.NotFound;
import niccolosciucco.u5_w3_d5.exceptions.Custom.Unauthorized;
import niccolosciucco.u5_w3_d5.utenti.entities.Utente;
import niccolosciucco.u5_w3_d5.utenti.enums.RuoloUtente;
import niccolosciucco.u5_w3_d5.utenti.services.UtenteService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EventoService {

    private final EventoRepository eventoRepository;
    private final UtenteService utenteService;

    public EventoService(EventoRepository eventoRepository, UtenteService utenteService) {
        this.eventoRepository = eventoRepository;
        this.utenteService = utenteService;
    }

    public Page<Evento> getAll(int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return this.eventoRepository.findAll(pageable);
    }

    public Evento findById(UUID id) {
        return this.eventoRepository.findById(id)
                .orElseThrow(() -> new NotFound("Evento con ID " + id + " non trovato"));
    }

    public Evento post(EventoDTO dto) {
        Utente organizzatore = this.utenteService.findById(dto.organizzatoreId());

        if (organizzatore.getRuolo() != RuoloUtente.ORGANIZZATORE) {
            throw new Unauthorized("Solo gli organizzatori possono creare eventi!");
        }

        Evento nuovoEvento = new Evento(dto.titolo(), dto.descrizione(), dto.data(), dto.luogo(), dto.postiMassimi(), dto.postiMassimi(), organizzatore);
        return this.eventoRepository.save(nuovoEvento);
    }

    public Evento put(UUID id, EventoDTO dto) {
        Evento found = this.findById(id);

        if (!(found.getPostiMassimi() == (dto.postiMassimi()))) {
            int postiPrenotati = found.getPostiMassimi() - found.getPostiDisponibili();
            if (dto.postiMassimi() < postiPrenotati) {
                throw new BadRequest("Impossibile ridurre i posti massimi a " + dto.postiMassimi() +
                        " perché ci sono già " + postiPrenotati + " prenotazioni attive.");
            }
            found.setPostiMassimi(dto.postiMassimi());
            found.setPostiDisponibili(dto.postiMassimi() - postiPrenotati);
        }

        found.setTitolo(dto.titolo());
        found.setDescrizione(dto.descrizione());
        found.setData(dto.data());
        found.setLuogo(dto.luogo());

        return this.eventoRepository.save(found);
    }

    public void delete(UUID id) {
        Evento found = this.findById(id);
        this.eventoRepository.delete(found);
    }
}
