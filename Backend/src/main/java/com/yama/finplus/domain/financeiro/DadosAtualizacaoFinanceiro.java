package com.yama.finplus.domain.financeiro;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yama.finplus.domain.financeiro.enums.Categoria;
import com.yama.finplus.domain.financeiro.enums.FormaPagamento;
import com.yama.finplus.domain.financeiro.enums.TipoMovimentacao;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DadosAtualizacaoFinanceiro(
        String nome,
        BigDecimal valor,
        String descricao,
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDateTime data,
        TipoMovimentacao tipo,
        Categoria categoria,
        FormaPagamento formaPagamento
) {
}
