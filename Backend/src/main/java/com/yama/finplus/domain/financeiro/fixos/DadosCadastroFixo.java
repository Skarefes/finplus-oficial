package com.yama.finplus.domain.financeiro.fixos;

import com.yama.finplus.domain.financeiro.enums.Categoria;
import com.yama.finplus.domain.financeiro.enums.TipoMovimentacao;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record DadosCadastroFixo(
        @NotBlank @Size(max = 100) String nome,
        @NotNull @Positive BigDecimal valor,
        @NotNull TipoMovimentacao tipo,
        @NotNull Categoria categoria,
        @NotNull @Min(1) @Max(28) Integer diaDoMes // até 28: evita o problema de fevereiro

) {}