package niccolosciucco.u5_w3_d5.utenti.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import niccolosciucco.u5_w3_d5.utenti.enums.RuoloUtente;

public record UtenteDTO(
        @NotBlank(message = "Lo username è obbligatorio")
        String username,

        @NotBlank(message = "La password è obbligatoria")
        @Size(min = 6, message = "La password deve contenere almeno 6 caratteri")
        String password,

        @NotNull(message = "Il ruolo è obbligatorio")
        RuoloUtente ruolo
) {
}
