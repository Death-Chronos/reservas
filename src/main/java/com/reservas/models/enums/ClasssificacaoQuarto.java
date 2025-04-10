package com.reservas.models.enums;

public enum ClasssificacaoQuarto {
    PADRAO("Padrão"),
    LUXUOSO("Luxuoso"),
    SUITE("Suíte");

    private String classificacaoQuarto;

    ClasssificacaoQuarto(String classificacaoQuarto) {
        this.classificacaoQuarto = classificacaoQuarto;
    }

    public String getClassificacao() {
        return classificacaoQuarto;
    }
}
