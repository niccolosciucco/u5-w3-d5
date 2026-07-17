package niccolosciucco.u5_w3_d5.prenotazioni.controllers;

import niccolosciucco.u5_w3_d5.prenotazioni.DTO.PrenotazioneDTO;
import niccolosciucco.u5_w3_d5.prenotazioni.entities.Prenotazione;
import niccolosciucco.u5_w3_d5.prenotazioni.services.PrenotazioneService;
import niccolosciucco.u5_w3_d5.utenti.entities.Utente;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/prenotazioni")
public class PrenotazioniController {
    private final PrenotazioneService prenotazioneService;

    public PrenotazioniController(PrenotazioneService prenotazioneService) {
        this.prenotazioneService = prenotazioneService;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('UTENTE_NORMALE', 'ORGANIZZATORE')")
    public Prenotazione findById(@PathVariable UUID id) {
        return this.prenotazioneService.findById(id);
    }

    @GetMapping("/me")
    @PreAuthorize("hasAuthority('UTENTE_NORMALE')")
    public List<Prenotazione> getMyPrenotazioni(@AuthenticationPrincipal Utente utenteLoggato) {
        return this.prenotazioneService.findByUtente(utenteLoggato);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('UTENTE_NORMALE')")
    public Prenotazione post(@RequestBody @Validated PrenotazioneDTO body,
                             @AuthenticationPrincipal Utente utenteLoggato) {
        return this.prenotazioneService.post(body, utenteLoggato);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('UTENTE_NORMALE', 'ORGANIZZATORE')")
    public void delete(@PathVariable UUID id, @AuthenticationPrincipal Utente utenteLoggato) {
        this.prenotazioneService.delete(id, utenteLoggato);
    }
}