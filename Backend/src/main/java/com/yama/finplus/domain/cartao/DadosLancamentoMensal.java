package com.yama.finplus.domain.financeiro.fixos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yama.finplus.domain.cartao.Parcela;
import com.yama.finplus.domain.financeiro.enums.Categoria;
import com.yama.finplus.domain.financeiro.enums.FormaPagamento;
import com.yama.finplus.domain.financeiro.enums.TipoMovimentacao;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DadosLancamentoMensal(
        Long id,                      // id do Financeiro (para editar, excluir e ver parcelas)
        String nome,
        BigDecimal valor,             // valor DESTE mês (o da parcela)
        BigDecimal valorTotal,        // valor da compra inteira
        String descricao,
        @JsonFormat(pattern = "dd/MM/yyyy") LocalDateTime data,
        Categoria categoria,
        TipoMovimentacao tipo,
        FormaPagamento formaPagamento,
        Integer quantidadeParcelas,
        Integer numeroParcela,
        boolean fixo
) {
    public DadosLancamentoMensal(Parcela p) {
        this(p.getFinanceiro().getId(),
                p.getFinanceiro().getNome(),
                p.getValorParcela(),
                p.getFinanceiro().getValor(),
                p.getFinanceiro().getDescricao(),
                p.getFinanceiro().getData(),
                p.getFinanceiro().getCategoria(),
                p.getFinanceiro().getTipo(),
                p.getFinanceiro().getFormaPagamento(),
                p.getTotalParcelas(),
                p.getNumeroParcela(),
                p.getFinanceiro().getCompetenciaFixo() != null);
    }
}
