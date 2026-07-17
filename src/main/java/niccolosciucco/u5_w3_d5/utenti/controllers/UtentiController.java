package niccolosciucco.u5_w3_d5.utenti.controllers;

import niccolosciucco.u5_w3_d5.utenti.DTO.UtenteDTO;
import niccolosciucco.u5_w3_d5.utenti.entities.Utente;
import niccolosciucco.u5_w3_d5.utenti.services.UtenteService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/utenti")
public class UtentiController {
    private final UtenteService utenteService;

    public UtentiController(UtenteService utenteService) {
        this.utenteService = utenteService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ORGANIZZATORE')") // Solo gli organizzatori possono vedere l'elenco utenti
    public Page<Utente> getAll(@RequestParam(defaultValue = "0") int page) {
        return this.utenteService.getAll(page);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('UTENTE_NORMALE', 'ORGANIZZATORE')")
    // Chiunque sia loggato può cercare un utente per ID
    public Utente findById(@PathVariable UUID id) {
        return this.utenteService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Utente post(@RequestBody @Validated UtenteDTO body) {
        return this.utenteService.post(body);
    }

    @PutMapping("/me")
    @PreAuthorize("hasAnyAuthority('UTENTE_NORMALE', 'ORGANIZZATORE')") // Chiunque sia loggato può modificare se stesso
    public Utente putMe(@AuthenticationPrincipal Utente utenteLoggato, @RequestBody @Validated UtenteDTO body) {
        return this.utenteService.put(utenteLoggato.getId(), body);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ORGANIZZATORE')") // Solo l'organizzatore può modificare tutti gli utenti tramite ID
    public Utente put(@PathVariable UUID id, @RequestBody @Validated UtenteDTO body) {
        return this.utenteService.put(id, body);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ORGANIZZATORE')") // Solo gli organizzatori possono eliminare utenti
    public void delete(@PathVariable UUID id) {
        this.utenteService.delete(id);
    }
}