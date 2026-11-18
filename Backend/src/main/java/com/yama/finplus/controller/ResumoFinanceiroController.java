package com.yama.finplus.controller;

import com.yama.finplus.domain.financeiro.DadosResumoFinanceiro;
import com.yama.finplus.domain.resumo.ResumoFincanceiroService;
import com.yama.finplus.domain.usuario.DadosTeto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/resumo")
public class ResumoFinanceiroController {
    private final ResumoFincanceiroService resumoFincanceiroService;

    public ResumoFinanceiroController(ResumoFincanceiroService resumoFincanceiroService) {
        this.resumoFincanceiroService = resumoFincanceiroService;
    }

    @GetMapping("/soma-tudo")
    public ResponseEntity<DadosResumoFinanceiro>resumoFinancas(Authentication authentication){

        String email = authentication.getName();

        var somaTudo = resumoFincanceiroService.somaTipos(email);
        return ResponseEntity.ok(somaTudo);
    }

    @GetMapping("/saldo-total")
    public ResponseEntity<BigDecimal> saldoTotal(Authentication authentication){

        String email = authentication.getName();

        var saldoTot = resumoFincanceiroService.calcularSaldoTotal(email);
        return ResponseEntity.ok(saldoTot);
    }

    // ResumoFinanceiroController.java
    @GetMapping("/teto")
    public ResponseEntity<DadosTeto> buscarTeto(Authentication authentication) {
        return ResponseEntity.ok(resumoFincanceiroService.buscarTeto(authentication.getName()));
    }

    @PutMapping("/teto")
    public ResponseEntity<DadosTeto> definirTeto(@RequestBody @Valid DadosTeto dados, Authentication authentication) {
        return ResponseEntity.ok(resumoFincanceiroService.definirTeto(dados, authentication.getName()));
    }
}
