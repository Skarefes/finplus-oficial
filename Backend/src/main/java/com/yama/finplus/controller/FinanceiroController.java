package com.yama.finplus.controller;

import com.yama.finplus.domain.DadosFinanceiro;
import com.yama.finplus.domain.Financeiro;
import com.yama.finplus.service.FinanceiroService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
public class FinanceiroController {

    private final FinanceiroService service;

    public FinanceiroController(FinanceiroService service) {
        this.service = service;
    }

    @PostMapping("/registrar")
    public ResponseEntity<Financeiro> registrar(@RequestBody @Valid DadosFinanceiro dados) {
        var financeiro = service.transicao(dados);
        return ResponseEntity.ok(financeiro);
    }

}
