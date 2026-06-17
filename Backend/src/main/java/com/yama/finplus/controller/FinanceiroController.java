package com.yama.finplus.controller;

import com.yama.finplus.domain.DadosCadastroFinanceiro;
import com.yama.finplus.domain.DadosDetalhamentoFinanceiro;
import com.yama.finplus.service.FinanceiroService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FinanceiroController {

    private final FinanceiroService service;

    public FinanceiroController(FinanceiroService service) {
        this.service = service;
    }

    @PostMapping("/registrar")
    public ResponseEntity<DadosDetalhamentoFinanceiro> registrar(
            @RequestBody @Valid DadosCadastroFinanceiro dados) {
        var detalhamento = service.transicao(dados);
        return ResponseEntity.ok(detalhamento);
    }

}
