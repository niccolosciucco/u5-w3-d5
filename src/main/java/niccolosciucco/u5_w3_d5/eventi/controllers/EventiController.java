package niccolosciucco.u5_w3_d5.eventi.controllers;

import niccolosciucco.u5_w3_d5.eventi.DTO.EventoDTO;
import niccolosciucco.u5_w3_d5.eventi.entities.Evento;
import niccolosciucco.u5_w3_d5.eventi.services.EventoService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
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
    public Evento post(@RequestBody @Validated EventoDTO body) {
        return this.eventoService.post(body);
    }

    @PutMapping("/{id}")
    public Evento put(@PathVariable UUID id, @RequestBody @Validated EventoDTO body) {
        return this.eventoService.put(id, body);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        this.eventoService.delete(id);
    }
}
