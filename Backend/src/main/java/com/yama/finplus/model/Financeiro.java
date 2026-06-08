package com.yama.finplus.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Financeiro {
    private Long id;
    private String nome;
    private BigDecimal valor;
    private String descricao;
    private LocalDateTime data;
    private TipoMovimentacao tipo;
    private Categoria categoria;

}
