package com.yama.finplus.domain.financeiro;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yama.finplus.domain.cartao.Cartao;
import com.yama.finplus.domain.cartao.Parcela;
import com.yama.finplus.domain.financeiro.enums.Categoria;
import com.yama.finplus.domain.financeiro.enums.FormaPagamento;
import com.yama.finplus.domain.financeiro.enums.TipoMovimentacao;
import com.yama.finplus.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    @Enumerated(EnumType.STRING)
    private FormaPagamento formaPagamento;
    @ManyToOne
    @JoinColumn(name = "cartao_id")
    private Cartao cartao;
    //O relacionamento ja e controlado pelo atributo financeiro que existe dentro de Parcelas
    @OneToMany(mappedBy = "financeiro", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Parcela> parcelas;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false) // o usuario nao pode ter id nullo
    private Usuario usuario;

    public Financeiro() {}

    public Financeiro(DadosCadastroFinanceiro dados){
        this.data = LocalDateTime.now();
        this.nome = dados.nome();
        this.valor = dados.valor();
        this.descricao = dados.descricao();
        this.tipo = dados.tipo();
        this.formaPagamento = dados.formaPagamento();
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

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
