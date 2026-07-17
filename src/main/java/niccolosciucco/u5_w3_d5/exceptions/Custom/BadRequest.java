package niccolosciucco.u5_w3_d5.exceptions.Custom;

public class BadRequest extends RuntimeException {
    public BadRequest(String message) {
        super(message);
    }
}
