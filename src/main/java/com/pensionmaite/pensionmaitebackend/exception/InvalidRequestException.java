package com.pensionmaite.pensionmaitebackend.exception;

public class InvalidRequestException extends RuntimeException {

    /**
     * Constructor to receive message that gets passed to RuntimeException
     * @param message
     */
    public InvalidRequestException(String message) { super(message); }

    /**
     * Constructor to receive message and throwable that gets passed to RuntimeException
     * @param message
     */
    public InvalidRequestException(String message, Throwable ex) { super(message, ex); }
}
