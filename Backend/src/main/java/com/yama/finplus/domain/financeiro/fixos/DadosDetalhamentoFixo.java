package com.yama.finplus.domain.financeiro.fixos;

import com.yama.finplus.domain.financeiro.enums.Categoria;
import com.yama.finplus.domain.financeiro.enums.TipoMovimentacao;

import java.math.BigDecimal;

public record DadosDetalhamentoFixo(
        Long id, String nome, BigDecimal valor,
        TipoMovimentacao tipo, Categoria categoria, Integer diaDoMes) {
    public DadosDetalhamentoFixo(LancamentoFixo f){
        this(f.getId(), f.getNome(), f.getValor(), f.getTipo(), f.getCategoria(), f.getDiaDoMes());
    }
}
