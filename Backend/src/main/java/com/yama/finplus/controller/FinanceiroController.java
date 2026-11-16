package com.yama.finplus.controller;

import com.yama.finplus.domain.cartao.DadosDetalhamentoParcela;
import com.yama.finplus.domain.cartao.ParcelaService;
import com.yama.finplus.domain.financeiro.DadosAtualizacaoFinanceiro;
import com.yama.finplus.domain.financeiro.DadosCadastroFinanceiro;
import com.yama.finplus.domain.financeiro.DadosDetalhamentoFinanceiro;
import com.yama.finplus.domain.financeiro.DadosResumoFinanceiro;
import com.yama.finplus.domain.financeiro.enums.TipoMovimentacao;
import com.yama.finplus.domain.financeiro.FinanceiroService;
import com.yama.finplus.domain.resumo.ResumoFincanceiroService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RequestMapping("/financeiro")
@RestController
public class FinanceiroController {

    private final FinanceiroService service;
    private final ResumoFincanceiroService resumo;
    private final ParcelaService parcelaService;

    public FinanceiroController(FinanceiroService service, ResumoFincanceiroService resumo, ParcelaService parcelaService) {
        this.service = service;
        this.resumo = resumo;
        this.parcelaService = parcelaService;
    }

    @PostMapping("/registrar")
    public ResponseEntity<DadosDetalhamentoFinanceiro> registrar(
            @RequestBody @Valid DadosCadastroFinanceiro dados, Authentication authentication) {

        String email = authentication.getName();

        var detalhamento = service.registrar(dados, email);

        return ResponseEntity.status(HttpStatus.CREATED).body(detalhamento);
    }

    @GetMapping("/listar-tudo")
    public ResponseEntity<List<DadosDetalhamentoFinanceiro>> lerTodosDados(Authentication authentication) {
        String email = authentication.getName();
        var dados = service.listarTudo(email);
        return ResponseEntity.ok(dados);
    }

    @GetMapping("/filtro")
    public ResponseEntity<List<DadosDetalhamentoFinanceiro>> listarPorTipo(@RequestParam TipoMovimentacao tipo, Authentication authentication) {
        String email = authentication.getName();
        var tipos = service.listarPorTipo(tipo, email);
        return ResponseEntity.ok(tipos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoFinanceiro> atualizarDados(@PathVariable Long id, @Valid @RequestBody DadosAtualizacaoFinanceiro dados, Authentication authentication) {
        String email = authentication.getName();
        var editar = service.editarDados(id, dados,  email);
        return ResponseEntity.ok(editar);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarDados(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        service.removerDados(id, email);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/parcelas")
    public ResponseEntity<List<DadosDetalhamentoParcela>> listarParcelas(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        var parcelas = parcelaService.listarPorFinanceiro(id, email);

        return ResponseEntity.ok(parcelas);
    }
}
