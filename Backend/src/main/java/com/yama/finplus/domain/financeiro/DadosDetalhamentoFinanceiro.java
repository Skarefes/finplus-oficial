package com.yama.finplus.domain.financeiro;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yama.finplus.domain.enums.TipoMovimentacao;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DadosDetalhamentoFinanceiro(Long id,
                                          String nome,
                                          BigDecimal valor,
                                          String descricao,
                                          @JsonFormat(pattern = "dd/MM/yyyy")
                                          LocalDateTime data,
                                          TipoMovimentacao tipo) {

    public DadosDetalhamentoFinanceiro(Financeiro financeiro){
        this(financeiro.getId(), financeiro.getNome(), financeiro.getValor(), financeiro.getDescricao(),
                financeiro.getData(), financeiro.getTipo());
    }
}
