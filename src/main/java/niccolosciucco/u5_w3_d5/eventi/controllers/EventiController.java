package niccolosciucco.u5_w3_d5.eventi.controllers;

import niccolosciucco.u5_w3_d5.eventi.DTO.EventoDTO;
import niccolosciucco.u5_w3_d5.eventi.entities.Evento;
import niccolosciucco.u5_w3_d5.eventi.services.EventoService;
import niccolosciucco.u5_w3_d5.utenti.entities.Utente;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/eventi")
public class EventiController {
    private final EventoService eventoService;

    public EventiController(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    @GetMapping
    public Page<Evento> getAll(@RequestParam(defaultValue = "0") int page) {
        return this.eventoService.getAll(page);
    }

    @GetMapping("/{id}")
    public Evento findById(@PathVariable UUID id) {
        return this.eventoService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ORGANIZZATORE')") // Solo gli organizzatori possono creare eventi
    public Evento post(@RequestBody @Validated EventoDTO body,
                       @AuthenticationPrincipal Utente organizzatoreLoggato) {
        return this.eventoService.post(body, organizzatoreLoggato);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ORGANIZZATORE')")
    public Evento put(@PathVariable UUID id, @RequestBody @Validated EventoDTO body,
                      @AuthenticationPrincipal Utente organizzatoreLoggato) {
        return this.eventoService.put(id, body, organizzatoreLoggato);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ORGANIZZATORE')")
    public void delete(@PathVariable UUID id, @AuthenticationPrincipal Utente organizzatoreLoggato) {
        this.eventoService.delete(id, organizzatoreLoggato);
    }
}