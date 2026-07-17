package niccolosciucco.u5_w3_d5.exceptions.Custom;

public class AlreadyInDb extends RuntimeException {
    public AlreadyInDb(String message) {
        super(message);
    }
}
