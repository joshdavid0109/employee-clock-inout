package org.shared_classes;

public class UserNotExistingException extends RuntimeException{
    public UserNotExistingException(){
        super("User is not existing. Please register first.");
    }
    public UserNotExistingException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }
    public UserNotExistingException(Throwable cause) {
        super(cause);
    }
}
