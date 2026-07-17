package niccolosciucco.u5_w3_d5.prenotazioni.controllers;

import niccolosciucco.u5_w3_d5.prenotazioni.DTO.PrenotazioneDTO;
import niccolosciucco.u5_w3_d5.prenotazioni.entities.Prenotazione;
import niccolosciucco.u5_w3_d5.prenotazioni.services.PrenotazioneService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/prenotazioni")
public class PrenotazioniController {
    private final PrenotazioneService prenotazioneService;

    public PrenotazioniController(PrenotazioneService prenotazioneService) {
        this.prenotazioneService = prenotazioneService;
    }

    @GetMapping("/{id}")
    public Prenotazione findById(@PathVariable UUID id) {
        return this.prenotazioneService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Prenotazione post(@RequestBody @Validated PrenotazioneDTO body) {
        return this.prenotazioneService.post(body);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        this.prenotazioneService.delete(id);
    }
}
