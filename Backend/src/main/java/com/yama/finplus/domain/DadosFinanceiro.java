package com.yama.finplus.domain;

import com.yama.finplus.domain.enums.TipoMovimentacao;
import com.yama.finplus.service.FinanceiroService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DadosFinanceiro(
        @NotBlank (message = "O nome é obrigatório!") String nome,
        @NotNull (message = "O valor é obritatório!") BigDecimal valor,
        String descricao,
        @NotNull (message = "O tipo é obrigatório") TipoMovimentacao tipo
) {
    public DadosFinanceiro(Financeiro financeiro){
        this(financeiro.getNome(), financeiro.getValor(), financeiro.getDescricao(), financeiro.getTipo());
    }
}
