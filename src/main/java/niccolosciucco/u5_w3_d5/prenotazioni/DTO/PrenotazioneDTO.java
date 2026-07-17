package niccolosciucco.u5_w3_d5.prenotazioni.DTO;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record PrenotazioneDTO(
        @NotNull(message = "L'ID dell'evento da prenotare è obbligatorio")
        UUID eventoId
) {
}
