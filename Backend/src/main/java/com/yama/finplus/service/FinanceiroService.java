package com.yama.finplus.service;

import com.yama.finplus.domain.DadosFinanceiro;
import com.yama.finplus.domain.Financeiro;
import com.yama.finplus.repository.FinanceiroRepository;
import jakarta.transaction.Transactional;

public class FinanceiroService {

    private final FinanceiroRepository financeiroRepository;

    public FinanceiroService(FinanceiroRepository financeiroRepository) {
        this.financeiroRepository = financeiroRepository;
    }

    @Transactional
    public Financeiro gastos(DadosFinanceiro dados) {
        Financeiro financeiro = new Financeiro();
        financeiroRepository.save(financeiro);
        return financeiro;
    }
}
