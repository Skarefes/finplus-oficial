package com.yama.finplus.domain;

import com.yama.finplus.domain.enums.TipoMovimentacao;
import com.yama.finplus.service.FinanceiroService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DadosFinanceiro(
        @NotBlank String nome,
        @NotNull BigDecimal valor,
        String descricao,
        @NotNull TipoMovimentacao tipo
) {
    public DadosFinanceiro(Financeiro financeiro){
        this(financeiro.getNome(), financeiro.getValor(), financeiro.getDescricao(), financeiro.getTipo());
    }
}
