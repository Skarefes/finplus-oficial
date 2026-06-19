package com.yama.finplus.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yama.finplus.domain.enums.Categoria;
import com.yama.finplus.domain.enums.TipoMovimentacao;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "financeiro")
@Getter @Setter
public class Financeiro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private BigDecimal valor;
    private String descricao;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDateTime data;
    @Enumerated(EnumType.STRING)
    private TipoMovimentacao tipo;
    @Enumerated(EnumType.STRING)
    private Categoria categoria;

    public Financeiro() {}

    public Financeiro(DadosCadastroFinanceiro dados){
        this.data = LocalDateTime.now();
        this.nome = dados.nome();
        this.valor = dados.valor();
        this.descricao = dados.descricao();
        this.tipo = dados.tipo();
    }

    public void atualizarDados(DadosAtualizacaoFinanceiro dados) {
        if(dados.nome() != null){
            this.nome = dados.nome();
        }
        if (dados.valor() != null){
            this.valor = dados.valor();
        }
        if (dados.descricao() != null){
            this.descricao = dados.descricao();
        }
        if (dados.data() != null){
            this.data = dados.data();
        }
        if (dados.tipo() != null){
            this.tipo = dados.tipo();
        }
    }
}
