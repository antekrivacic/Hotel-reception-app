package com.reception.projektkrivacic.exceptions;

public class InvalidReservationDateException extends RuntimeException{

    public InvalidReservationDateException() {
    }

    public InvalidReservationDateException(String message) {
        super(message);
    }

    public InvalidReservationDateException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidReservationDateException(Throwable cause) {
        super(cause);
    }

    public InvalidReservationDateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
