package com.yama.finplus.domain.financeiro;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yama.finplus.domain.cartao.Parcela;
import com.yama.finplus.domain.financeiro.enums.Categoria;
import com.yama.finplus.domain.financeiro.enums.FormaPagamento;
import com.yama.finplus.domain.financeiro.enums.TipoMovimentacao;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DadosDetalhamentoFinanceiro(Long id,
                                          String nome,
                                          BigDecimal valor,
                                          String descricao,
                                          @JsonFormat(pattern = "dd/MM/yyyy")
                                          LocalDateTime data,
                                          Categoria categoria,
                                          TipoMovimentacao tipo,
                                          FormaPagamento formaPagamento

) {

    public DadosDetalhamentoFinanceiro(Financeiro financeiro){
        this(financeiro.getId(), financeiro.getNome(), financeiro.getValor(), financeiro.getDescricao(),
                financeiro.getData(), financeiro.getCategoria(),
                financeiro.getTipo(), financeiro.getFormaPagamento());
    }
}
