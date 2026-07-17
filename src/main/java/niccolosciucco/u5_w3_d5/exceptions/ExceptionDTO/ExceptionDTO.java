package niccolosciucco.u5_w3_d5.exceptions.ExceptionDTO;

import java.time.LocalDateTime;

public record ExceptionDTO(LocalDateTime localDateTime, String message) {
}
