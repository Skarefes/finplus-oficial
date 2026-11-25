package com.yama.finplus.controller;

import com.yama.finplus.domain.financeiro.fixos.DadosCadastroFixo;
import com.yama.finplus.domain.financeiro.fixos.DadosDetalhamentoFixo;
import com.yama.finplus.domain.financeiro.fixos.FixoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fixos")
public class FixoController {

    private final FixoService service;

    public FixoController(FixoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<DadosDetalhamentoFixo>> listar(Authentication authentication) {
        return ResponseEntity.ok(service.listar(authentication.getName()));
    }

    @PostMapping
    public ResponseEntity<DadosDetalhamentoFixo> criar(@RequestBody @Valid DadosCadastroFixo dados,
                                                       Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(dados, authentication.getName()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoFixo> atualizar(@PathVariable Long id,
                                                           @RequestBody @Valid DadosCadastroFixo dados,
                                                           Authentication authentication) {
        return ResponseEntity.ok(service.atualizar(id, dados, authentication.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id, Authentication authentication) {
        service.excluir(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
