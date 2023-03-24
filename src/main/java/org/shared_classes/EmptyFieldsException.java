package org.shared_classes;

public class EmptyFieldsException extends RuntimeException{
    public EmptyFieldsException(){
        super("Please fill out the fields.");
    }
    public EmptyFieldsException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }
    public EmptyFieldsException(Throwable cause) {
        super(cause);
    }
}
