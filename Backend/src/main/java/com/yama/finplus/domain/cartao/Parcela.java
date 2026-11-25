package com.yama.finplus.domain.cartao;

import com.yama.finplus.domain.financeiro.Financeiro;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "parcelas")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Parcela {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer numeroParcela;
    private Integer totalParcelas;
    private BigDecimal valorParcela;
    @Column(nullable = false) // mes em que a parcela aparece ( dia 1 de cada mes por enqunato), nao data de vencimento
    private LocalDate competencia;
    @Enumerated(EnumType.STRING)
    private StatusPagamento statusPagamento;
    @ManyToOne
    @JoinColumn(name = "financeiro_id", nullable = false)
    private Financeiro financeiro;

    public Parcela(Financeiro financeiro,
                   Integer numeroParcela,
                   Integer totalParcelas,
                   BigDecimal valorParcela,
                   LocalDate competencia) {
        this.financeiro = financeiro;
        this.numeroParcela = numeroParcela;
        this.totalParcelas = totalParcelas;
        this.valorParcela = valorParcela;
        this.competencia = competencia;
    }
}
