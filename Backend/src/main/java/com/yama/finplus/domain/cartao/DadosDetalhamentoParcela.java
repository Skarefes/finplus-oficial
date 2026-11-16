package com.yama.finplus.domain.cartao;

import java.math.BigDecimal;

public record DadosDetalhamentoParcela(
        Long id,
        Integer numeroParcela,
        Integer totalParcela,
        BigDecimal valorParcela
) {

    public DadosDetalhamentoParcela(Parcela parcela) {
        this(
                parcela.getId(),
                parcela.getNumeroParcela(),
                parcela.getTotalParcelas(),
                parcela.getValorParcela()
        );
    }
}
