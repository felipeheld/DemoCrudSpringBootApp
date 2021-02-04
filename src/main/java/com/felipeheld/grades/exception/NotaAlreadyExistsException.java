package com.felipeheld.grades.exception;


@SuppressWarnings("serial")
public class NotaAlreadyExistsException extends Exception {

    public NotaAlreadyExistsException(String message) {
        super(message);        
    }
}
