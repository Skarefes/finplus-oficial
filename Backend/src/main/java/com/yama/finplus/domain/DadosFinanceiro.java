package com.yama.finplus.domain;

import com.yama.finplus.domain.enums.TipoMovimentacao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record DadosFinanceiro(
       @NotBlank String nome,
        @NotNull BigDecimal valor,
        String descricao,
        @NotNull TipoMovimentacao tipo
) {
}
