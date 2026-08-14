package com.yama.finplus.domain.cartao;

import com.yama.finplus.domain.financeiro.Financeiro;
import com.yama.finplus.repository.ParcelaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class ParcelaService {
    private final ParcelaRepository parcelaRepository;

    public ParcelaService(ParcelaRepository parcelaRepository) {
        this.parcelaRepository = parcelaRepository;
    }

    public void gerarParcelas(Financeiro financeiro, Integer quantidadeParcelas) {
        int quantidade =  (quantidadeParcelas == null)? 1:quantidadeParcelas;

        //calcular o valor de uma parcela
        List<Parcela> parcelas = new ArrayList<>();
        //pega o valor total do Financeiro e divide pela quantidade de parcelas para descobrir o valor de cada parcela.
        BigDecimal valorParcela = financeiro.getValor().divide(BigDecimal.valueOf(quantidade));
        for (int i = 1; i <= quantidade; i++) {
           Parcela parcela = new Parcela(financeiro, i, quantidade ,valorParcela);
           parcelas.add(parcela);
        }

        parcelaRepository.saveAll((parcelas));
    }
}
