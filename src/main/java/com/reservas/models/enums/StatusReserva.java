package com.reservas.models.enums;

public enum StatusReserva {
    ATIVA("Ativa"),
    CANCELADA("Cancelada"),
    FINALIZADA("Finalizada");

    private String status;

    StatusReserva(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
