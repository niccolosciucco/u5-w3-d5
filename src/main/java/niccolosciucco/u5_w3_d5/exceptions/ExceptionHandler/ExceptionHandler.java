package niccolosciucco.u5_w3_d5.exceptions.ExceptionHandler;

import niccolosciucco.u5_w3_d5.exceptions.Custom.AlreadyInDb;
import niccolosciucco.u5_w3_d5.exceptions.Custom.BadRequest;
import niccolosciucco.u5_w3_d5.exceptions.Custom.NotFound;
import niccolosciucco.u5_w3_d5.exceptions.Custom.Unauthorized;
import niccolosciucco.u5_w3_d5.exceptions.ExceptionDTO.ExceptionDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(NotFound.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDTO handleNotFound(NotFound ex) {
        return new ExceptionDTO(LocalDateTime.now(), ex.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(BadRequest.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDTO handleBadRequest(BadRequest ex) {
        return new ExceptionDTO(LocalDateTime.now(), ex.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(Unauthorized.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ExceptionDTO handleUnauthorized(Unauthorized ex) {
        return new ExceptionDTO(LocalDateTime.now(), ex.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(AlreadyInDb.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDTO handleAlreadyInDb(AlreadyInDb ex) {
        return new ExceptionDTO(LocalDateTime.now(), ex.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionDTO handleGenericException(Exception ex) {
        ex.printStackTrace();
        return new ExceptionDTO(LocalDateTime.now(), ex.getMessage());
    }
}