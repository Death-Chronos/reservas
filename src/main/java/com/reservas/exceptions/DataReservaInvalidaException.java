package com.reservas.exceptions;

public class DataReservaInvalidaException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public DataReservaInvalidaException(String message) {
        super(message);
    }
    
}
