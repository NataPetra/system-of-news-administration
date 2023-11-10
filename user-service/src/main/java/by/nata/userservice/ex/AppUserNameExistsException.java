package by.nata.userservice.ex;

public class AppUserNameExistsException extends RuntimeException {

    public AppUserNameExistsException(String message) {
        super(message);
    }
}
