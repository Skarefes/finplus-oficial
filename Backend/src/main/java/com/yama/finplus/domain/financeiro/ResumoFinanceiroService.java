package com.yama.finplus.domain.financeiro;

import com.yama.finplus.domain.financeiro.enums.TipoMovimentacao;
import com.yama.finplus.repository.ParcelaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Optional;

@Service
public class ResumoFinanceiroService {
    private final ParcelaRepository parcelaRepository;

    public ResumoFinanceiroService(ParcelaRepository parcelaRepository) {
        this.parcelaRepository = parcelaRepository;
    }

    public DadosResumoFinanceiro somaTipos(String email) {
        // o que ainda vai vencer (parcelas futuras, fixos já gerados) não entra no saldo de hoje
        LocalDate ate = YearMonth.now().atDay(1);

        BigDecimal receitas = Optional.ofNullable(
                parcelaRepository.somarAteCompetencia(TipoMovimentacao.RECEITA, email, ate)).orElse(BigDecimal.ZERO);
        BigDecimal despesas = Optional.ofNullable(
                parcelaRepository.somarAteCompetencia(TipoMovimentacao.DESPESA, email, ate)).orElse(BigDecimal.ZERO);

        return new DadosResumoFinanceiro(receitas, despesas);
    }

    public BigDecimal calcularSaldoTotal(String email) {
        DadosResumoFinanceiro resumo = this.somaTipos(email);
        return resumo.totalReceita().subtract(resumo.totalDespesa());
    }

}
