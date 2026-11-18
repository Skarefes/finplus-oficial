package com.yama.finplus.domain.usuario;

import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record DadosTeto(
        @Positive(message = "O limite deve ser maior que zero")BigDecimal teto
        ) {
}
