package com.yama.finplus.domain.cartao;

import com.yama.finplus.domain.financeiro.Financeiro;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "parcelas")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Parcela {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer numeroParcelas;
    private Integer totalParcelas;
    private BigDecimal valorParcela;
    @Enumerated(EnumType.STRING)
    private StatusPagamento statusPagamento;
    @ManyToOne
    private Financeiro financeiro;
}
