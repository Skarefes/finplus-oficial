package com.yama.finplus.domain;

import java.math.BigDecimal;

public record DadosResumoFinanceiro(
        BigDecimal totalReceita,
        BigDecimal totalDespesa) {
}
