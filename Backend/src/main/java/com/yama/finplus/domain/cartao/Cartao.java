package com.yama.finplus.domain.cartao;

import com.yama.finplus.domain.financeiro.Financeiro;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "cartao")
@Getter
@Setter
@NoArgsConstructor
public class Cartao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String proprietario;
    private String banco;
    private String finalCartao;
    @OneToMany(mappedBy = "cartao")
    private List<Financeiro> financeiros;
}
