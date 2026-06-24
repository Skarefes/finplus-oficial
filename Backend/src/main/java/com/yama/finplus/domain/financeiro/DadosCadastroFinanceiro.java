package com.yama.finplus.domain.financeiro;

import com.yama.finplus.domain.cartao.Parcela;
import com.yama.finplus.domain.financeiro.enums.FormaPagamento;
import com.yama.finplus.domain.financeiro.enums.TipoMovimentacao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record DadosCadastroFinanceiro(
        @NotBlank (message = "O nome é obrigatório!") String nome,
        @NotNull (message = "O valor é obritatório!") BigDecimal valor,
        String descricao,
        @NotNull (message = "O tipo é obrigatório") TipoMovimentacao tipo,
        FormaPagamento formaPagamento,
        Integer quantidadeParcela
) {
    public DadosCadastroFinanceiro(Financeiro financeiro) {
        this(financeiro.getNome(), financeiro.getValor(), financeiro.getDescricao(), financeiro.getTipo(), financeiro.getFormaPagamento(), null);
    }
}
