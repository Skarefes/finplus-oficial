package com.yama.finplus.controller;

import com.yama.finplus.domain.DadosAtualizacaoFinanceiro;
import com.yama.finplus.domain.DadosCadastroFinanceiro;
import com.yama.finplus.domain.DadosDetalhamentoFinanceiro;
import com.yama.finplus.domain.DadosResumoFinanceiro;
import com.yama.finplus.domain.enums.TipoMovimentacao;
import com.yama.finplus.service.FinanceiroService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RequestMapping("/financeiro")
@RestController
public class FinanceiroController {

    private final FinanceiroService service;

    public FinanceiroController(FinanceiroService service) {
        this.service = service;
    }

    @PostMapping("/registrar")
    public ResponseEntity<DadosDetalhamentoFinanceiro> registrar(
            @RequestBody @Valid DadosCadastroFinanceiro dados) {
        var detalhamento = service.registrar(dados);
        return ResponseEntity.status(HttpStatus.CREATED).body(detalhamento);
    }

    @GetMapping("/listar-tudo")
    public ResponseEntity<List<DadosDetalhamentoFinanceiro>> lerTodosDados(){
        var dados = service.listarTudo();
        return ResponseEntity.ok(dados);
    }

    @GetMapping("/filtro")
    public ResponseEntity<List<DadosDetalhamentoFinanceiro>> listarPorTipo(@RequestParam TipoMovimentacao tipo) {
        var tipos = service.listarPorTipo(tipo);
        return ResponseEntity.ok(tipos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoFinanceiro> atualizarDados(@PathVariable Long id, @Valid @RequestBody DadosAtualizacaoFinanceiro dados) {
        var editar = service.editarDados(id, dados);
        return ResponseEntity.ok(editar);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarDados(@PathVariable Long id) {
        service.removerDados(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/soma-tudo")
    public ResponseEntity<DadosResumoFinanceiro>somarFinancas(){
        var somaTudo = service.somaTipos();
        return ResponseEntity.ok(somaTudo);
    }

    @GetMapping("/saldo-total")
    public ResponseEntity<BigDecimal> saldoTotal(){
        var saldoTot = service.calcularSaldo();
        return ResponseEntity.ok(saldoTot);
    }
}
