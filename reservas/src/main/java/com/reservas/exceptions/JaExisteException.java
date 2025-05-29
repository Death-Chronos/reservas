package com.reservas.exceptions;

public class JaExisteException extends RuntimeException {

    public JaExisteException(String message) {
        super(message);
    }
}
