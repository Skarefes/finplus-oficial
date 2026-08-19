package com.yama.finplus.infra.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class TratadorDeErros {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<DadosErro>> tratrarErro400(MethodArgumentNotValidException ex) {
        var erros = ex.getFieldErrors().stream().map(DadosErro::new).toList();
        return ResponseEntity.badRequest().body(erros);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> tratarErroJson(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body("Tipo de dado invalido ou mal formatado!");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> tratarErro500(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro: " + ex.getLocalizedMessage());
    }


    @ExceptionHandler(FormaPagamentoNaoAutorizadaException.class)
    public ResponseEntity<String> tratarFormaPagamentoNaoAutorizada(FormaPagamentoNaoAutorizadaException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

}
