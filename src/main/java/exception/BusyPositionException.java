package exception;

public class BusyPositionException extends RuntimeException {
    public BusyPositionException(String message) {
        super(message);
    }
}
