package com.yama.finplus.domain.financeiro.fixos;

import com.yama.finplus.domain.financeiro.enums.Categoria;
import com.yama.finplus.domain.financeiro.enums.TipoMovimentacao;
import com.yama.finplus.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "lancamento_fixo")
@Getter
@Setter
@NoArgsConstructor
public class LancamentoFixo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
    private String nome;
    private BigDecimal valor;
    @Enumerated(EnumType.STRING)
    private TipoMovimentacao tipo;
    @Enumerated(EnumType.STRING)
    private Categoria categoria;
    @Column(name = "dia_do_mes")
    private Integer diaDoMes;
    @Column(name = "gerado_ate")
    private LocalDate geradoAte;

}
