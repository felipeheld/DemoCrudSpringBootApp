package com.felipeheld.grades.exception;


@SuppressWarnings("serial")
public class NotaDoesNotExistException extends Exception {

    public NotaDoesNotExistException(String message) {
        super(message);
    }
}
