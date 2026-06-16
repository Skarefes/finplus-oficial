package com.yama.finplus.service;

import com.yama.finplus.domain.DadosFinanceiro;
import com.yama.finplus.domain.Financeiro;
import com.yama.finplus.repository.FinanceiroRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class FinanceiroService {

    private final FinanceiroRepository financeiroRepository;

    public FinanceiroService(FinanceiroRepository financeiroRepository) {
        this.financeiroRepository = financeiroRepository;
    }

    //Função para colocar as transicoes de gastos e ganhos
    @Transactional
    public Financeiro transicao(DadosFinanceiro dados) {
        Financeiro financeiro = new Financeiro(dados);
        financeiroRepository.save(financeiro);
        return financeiro;
    }
}
