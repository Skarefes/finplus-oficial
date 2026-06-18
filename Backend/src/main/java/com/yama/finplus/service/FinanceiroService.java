package com.yama.finplus.service;

import com.yama.finplus.domain.DadosCadastroFinanceiro;
import com.yama.finplus.domain.DadosDetalhamentoFinanceiro;
import com.yama.finplus.domain.Financeiro;
import com.yama.finplus.domain.enums.TipoMovimentacao;
import com.yama.finplus.repository.FinanceiroRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FinanceiroService {

    private final FinanceiroRepository financeiroRepository;

    public FinanceiroService(FinanceiroRepository financeiroRepository) {
        this.financeiroRepository = financeiroRepository;
    }

    //Função para colocar as transicoes de gastos e ganhos
    @Transactional
    public DadosDetalhamentoFinanceiro registrar(DadosCadastroFinanceiro dados) {
        var financeiro = new Financeiro(dados);
        financeiroRepository.save(financeiro);
        return new DadosDetalhamentoFinanceiro(financeiro);
    }

    //Função para pegar todos os dados
    public List<DadosDetalhamentoFinanceiro> listarTudo() {
        return financeiroRepository.findAll().stream()
                .map(DadosDetalhamentoFinanceiro::new).toList();
    }

    //Função que vai filtrar a lista do tipo e enviar conforme requisitado pelo URL
    public List<DadosDetalhamentoFinanceiro> listarPorTipo(TipoMovimentacao tipo) {
        return financeiroRepository.findByTipo(tipo).stream()
                .map(DadosDetalhamentoFinanceiro::new).toList();
    }
}
