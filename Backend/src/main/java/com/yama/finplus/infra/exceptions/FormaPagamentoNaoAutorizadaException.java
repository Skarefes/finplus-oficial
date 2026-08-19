package com.yama.finplus.infra.exceptions;

public class FormaPagamentoNaoAutorizadaException extends RuntimeException {
    public FormaPagamentoNaoAutorizadaException(String message) {
        super(message);
    }
}
