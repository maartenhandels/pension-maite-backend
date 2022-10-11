package com.pensionmaite.pensionmaitebackend.exception;

public class UniqueConstraintException extends RuntimeException {

    /**
     * Constructor to receive message that gets passed to RuntimeException
     * @param message
     */
    public UniqueConstraintException(String message) { super(message); }

    /**
     * Constructor to receive message and throwable that gets passed to RuntimeException
     * @param message
     */
    public UniqueConstraintException(String message, Throwable ex) { super(message, ex); }
}
