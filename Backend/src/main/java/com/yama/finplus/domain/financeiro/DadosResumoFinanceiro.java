package com.yama.finplus.domain.financeiro;

import java.math.BigDecimal;

public record DadosResumoFinanceiro(
        BigDecimal totalReceita,
        BigDecimal totalDespesa) {
}
