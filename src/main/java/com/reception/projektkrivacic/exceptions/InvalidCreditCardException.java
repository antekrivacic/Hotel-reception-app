package com.reception.projektkrivacic.exceptions;

public class InvalidCreditCardException extends RuntimeException{

        public InvalidCreditCardException() {
        }

        public InvalidCreditCardException(String message) {
            super(message);
        }

        public InvalidCreditCardException(String message, Throwable cause) {
            super(message, cause);
        }

        public InvalidCreditCardException(Throwable cause) {
            super(cause);
        }

        public InvalidCreditCardException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }
}
