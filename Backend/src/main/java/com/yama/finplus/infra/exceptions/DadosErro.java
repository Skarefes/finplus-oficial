package com.yama.finplus.infra.exceptions;

import org.springframework.validation.FieldError;

public record DadosErro(String campo, String mensagem) {
    public DadosErro(FieldError erro){
        this(erro.getField(), erro.getDefaultMessage());
    }
}
