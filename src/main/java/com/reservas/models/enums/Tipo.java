package com.reservas.models.enums;

public enum Tipo {
    SOLTEIRO(80),
    CASAL(120),
    FAMILIA(230),
    SUITE(300),
    DUPLEX(500);

    private final double preco;

    Tipo(double preco) {
        this.preco = preco;
    }

    public double getPreco() {
        return preco;
    }
}
