package org.shared_classes;

public class CredentialsErrorException extends RuntimeException {
    String errorMessage;
    public CredentialsErrorException(){
        super("Invalid username/password.");
    }
    public CredentialsErrorException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }
    public CredentialsErrorException(Throwable cause) {
        super(cause);
    }
}
