package com.pensionmaite.pensionmaitebackend.exception;

public class InvalidContactDataException extends RuntimeException {

    /**
     * Constructor to receive message that gets passed to RuntimeException
     * @param message
     */
    public InvalidContactDataException(String message) { super(message); }

    /**
     * Constructor to receive message and throwable that gets passed to RuntimeException
     * @param message
     */
    public InvalidContactDataException(String message, Throwable ex) { super(message, ex); }
}
