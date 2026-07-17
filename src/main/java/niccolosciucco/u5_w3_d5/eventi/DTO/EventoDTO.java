package niccolosciucco.u5_w3_d5.eventi.DTO;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public record EventoDTO(
        @NotBlank(message = "Il titolo dell'evento è obbligatorio")
        @Size(max = 100, message = "Il titolo non può superare i 100 caratteri")
        String titolo,

        @NotBlank(message = "La descrizione è obbligatoria")
        @Size(max = 1000, message = "La descrizione non può superare i 1000 caratteri")
        String descrizione,

        @NotNull(message = "La data e l'ora dell'evento sono obbligatorie")
        @Future(message = "La data dell'evento deve essere nel futuro")
        LocalDateTime data,

        @NotBlank(message = "Il luogo dell'evento è obbligatorio")
        String luogo,

        @NotNull(message = "Il numero di posti massimi è obbligatorio")
        @Min(value = 1, message = "L'evento deve avere almeno 1 posto disponibile")
        Integer postiMassimi
) {
}