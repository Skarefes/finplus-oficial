package com.yama.finplus.domain.resumo;

import com.yama.finplus.domain.cartao.ParcelaService;
import com.yama.finplus.domain.financeiro.DadosResumoFinanceiro;
import com.yama.finplus.domain.financeiro.enums.TipoMovimentacao;
import com.yama.finplus.repository.FinanceiroRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class ResumoFincanceiroService {

    final private FinanceiroRepository financeiroRepository;

    public ResumoFincanceiroService(FinanceiroRepository financeiroRepository, ParcelaService parcelaService) {
        this.financeiroRepository = financeiroRepository;

    }

    //Função que vai somar as receitas e despesas totais, para cada usuario
    public DadosResumoFinanceiro somaTipos(String email) {
        BigDecimal receitas = Optional.ofNullable(financeiroRepository
                .somarPorTipoEUsuario(TipoMovimentacao.RECEITA, email)).orElse(BigDecimal.ZERO);

        BigDecimal despesas = Optional.ofNullable(financeiroRepository
                .somarPorTipoEUsuario(TipoMovimentacao.DESPESA, email)).orElse(BigDecimal.ZERO);

        return new DadosResumoFinanceiro(receitas, despesas);
    }

    //Função que vai analisar o saldo total entre receita e despesas, para cada usuario
    public BigDecimal calcularSaldoTotal(String email) {
        DadosResumoFinanceiro resumo =  this.somaTipos(email);
        return resumo.totalReceita().subtract(resumo.totalDespesa());
    }

}
