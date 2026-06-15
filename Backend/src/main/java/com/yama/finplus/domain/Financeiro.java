package com.yama.finplus.domain;

import com.yama.finplus.domain.enums.Categoria;
import com.yama.finplus.domain.enums.TipoMovimentacao;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "financeiro")
public class Financeiro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private BigDecimal valor;
    private String descricao;
    private LocalDateTime data;
    private TipoMovimentacao tipo;
    private Categoria categoria;

    public Financeiro() {}


}
