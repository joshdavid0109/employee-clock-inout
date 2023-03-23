package org.shared_classes;

public class WrongPasswordException extends RuntimeException {
    public WrongPasswordException(){
        super("Wrong password");
    }
    public WrongPasswordException(String errorMessage) {
        super(errorMessage);
    }
    public WrongPasswordException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }
    public WrongPasswordException(Throwable cause) {
        super(cause);
    }
}
