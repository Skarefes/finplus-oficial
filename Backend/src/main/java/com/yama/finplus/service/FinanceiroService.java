package com.yama.finplus.service;

import com.yama.finplus.domain.*;
import com.yama.finplus.domain.enums.TipoMovimentacao;
import com.yama.finplus.repository.FinanceiroRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

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

    //Função para filtrar a lista do tipo e enviar conforme requisitado pelo URL
    public List<DadosDetalhamentoFinanceiro> listarPorTipo(TipoMovimentacao tipo) {
        return financeiroRepository.findByTipo(tipo).stream()
                .map(DadosDetalhamentoFinanceiro::new).toList();
    }

    @Transactional
    //Função para editar um item
    public DadosDetalhamentoFinanceiro editarDados(Long id, DadosAtualizacaoFinanceiro dados) {
        //identificador fincaneiro ele pega o repository do Financeiro que ja é o objeto pra poder editar
        var identificadorFinanceiro = financeiroRepository.findById(id).orElseThrow();
        identificadorFinanceiro.atualizarDados(dados);
        //retorna um novo DTO com os novos dados
        return new DadosDetalhamentoFinanceiro(identificadorFinanceiro);
    }

    //Função que deleta um item
    public void removerDados(Long id) {
        financeiroRepository.deleteById(id);
    }

    //Função que vai somar as receitas e despesas
    public DadosResumoFinanceiro somaTipos() {
        BigDecimal receitas = Optional.ofNullable(financeiroRepository
                .findByTipoAndSumTipo(TipoMovimentacao.RECEITA)).orElse(BigDecimal.ZERO);

        BigDecimal despesas = Optional.ofNullable(financeiroRepository
                .findByTipoAndSumTipo(TipoMovimentacao.DESPESA)).orElse(BigDecimal.ZERO);

        return new DadosResumoFinanceiro(receitas, despesas);
    }

    //Função que vai analisar o saldo total entre receita e despesas
    public BigDecimal calcularSaldo(){
        DadosResumoFinanceiro resumo =  this.somaTipos();
        return resumo.totalReceita().subtract(resumo.totalDespesa());
    }
}
