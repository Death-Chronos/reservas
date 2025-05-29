package com.reservas.models.enums;

public enum TipoQuarto {
    SOLTEIRO("Solteiro"),
    CASAL("Casal"),
    DUPLEX("Duplex"),
    DORMITORIO("Dormitório");

    private String tipoQuarto;

    TipoQuarto(String tipoQuarto){
        this.tipoQuarto = tipoQuarto;
    }

    public String getTipo() {
        return tipoQuarto;
    }

    
    
}
