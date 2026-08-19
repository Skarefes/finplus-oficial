package com.yama.finplus.domain.financeiro.enums;

public enum FormaPagamento {
    PIX(false),
    DEBITO(false),
    CREDITO(true),
    DINHEIRO(false),
    BOLETO(true);

    private final boolean permiteParcelamento;

    FormaPagamento(boolean permiteParcelamento) {
        this.permiteParcelamento = permiteParcelamento;
    }

    public boolean permiteParcelamento(){
        return permiteParcelamento;
    }
}
