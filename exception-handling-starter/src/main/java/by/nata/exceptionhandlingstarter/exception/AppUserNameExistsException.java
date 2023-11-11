package by.nata.exceptionhandlingstarter.exception;

public class AppUserNameExistsException extends RuntimeException {

    public AppUserNameExistsException(String message) {
        super(message);
    }
}
