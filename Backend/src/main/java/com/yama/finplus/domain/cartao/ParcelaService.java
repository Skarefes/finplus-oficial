package com.yama.finplus.domain.cartao;

import com.yama.finplus.domain.financeiro.Financeiro;
import com.yama.finplus.repository.ParcelaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class ParcelaService {
    private final ParcelaRepository parcelaRepository;

    public ParcelaService(ParcelaRepository parcelaRepository) {
        this.parcelaRepository = parcelaRepository;
    }

    public void gerarParcelas(Financeiro financeiro, Integer quantidadeParcelas) {
        int quantidade = (quantidadeParcelas == null) ? 1 : quantidadeParcelas;

        //calcular o valor de uma parcela
        List<Parcela> parcelas = new ArrayList<>();
        //pega o valor total do Financeiro e divide pela quantidade de parcelas para descobrir o valor de cada parcela.
        //Iremos fazer com que seja limitado a 2 casas eusaremos o down porque queremos que a diferença fique para a última parcela
        //roundingmode é um enum que define como um número deve ser arredondado quando uma operação matemática resulta em mais casas decimais, ou com mais precisao de valor
        BigDecimal valorParcela = financeiro.getValor().divide(BigDecimal.valueOf(quantidade), 2, RoundingMode.DOWN);
        //valor distribuido começa com zero
        BigDecimal valorDistribuido = BigDecimal.ZERO;
        for (int i = 1; i <= quantidade; i++) {
            BigDecimal valorAtual = valorParcela;

            if (i == quantidade){
                valorAtual = financeiro.getValor().subtract(valorDistribuido);
            }

            Parcela parcela = new Parcela(financeiro, i, quantidade, valorAtual);
            parcelas.add(parcela);

            valorDistribuido = valorDistribuido.add(valorAtual);
        }

        parcelaRepository.saveAll((parcelas));
    }
}
