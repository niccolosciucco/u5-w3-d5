package niccolosciucco.u5_w3_d5.exceptions.Custom;

public class Unauthorized extends RuntimeException {
    public Unauthorized(String message) {
        super(message);
    }
}
