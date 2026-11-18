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
                                          FormaPagamento formaPagamento,
                                          Integer quantidadeParcelas

) {

    public DadosDetalhamentoFinanceiro(Financeiro financeiro){
        this(financeiro.getId(), financeiro.getNome(), financeiro.getValor(), financeiro.getDescricao(),
                financeiro.getData(), financeiro.getCategoria(),
                financeiro.getTipo(), financeiro.getFormaPagamento(),
                contarParcelas(financeiro));
    }

    // logo após o cadastro a lista de parcelas ainda é null nessa instância, então assume 1
    private static Integer contarParcelas(Financeiro financeiro) {
        var parcelas = financeiro.getParcelas();
        return (parcelas == null || parcelas.isEmpty()) ? 1 : parcelas.size();
    }
}
