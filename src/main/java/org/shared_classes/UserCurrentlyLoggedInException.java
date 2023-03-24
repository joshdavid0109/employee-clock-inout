package org.shared_classes;

public class UserCurrentlyLoggedInException extends RuntimeException{

    public UserCurrentlyLoggedInException(){
        super("User is currenly loggged in from another device.");
    }
    public UserCurrentlyLoggedInException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }
    public UserCurrentlyLoggedInException(Throwable cause) {
        super(cause);
    }
}
