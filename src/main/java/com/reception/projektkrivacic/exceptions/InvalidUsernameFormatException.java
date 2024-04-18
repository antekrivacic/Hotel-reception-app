package com.reception.projektkrivacic.exceptions;

public class InvalidUsernameFormatException extends Exception {

    public InvalidUsernameFormatException() {
    }

    public InvalidUsernameFormatException(String message) {
        super(message);
    }

    public InvalidUsernameFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidUsernameFormatException(Throwable cause) {
        super(cause);
    }

    public InvalidUsernameFormatException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
