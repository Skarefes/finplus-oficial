import { DadosDetalhamentoFinanceiro } from './dadosdetalhamento-financeiro';

// uma linha do mês: compra à vista, parcela do mês ou lançamento fixo
export interface DadosLancamentoMensal extends DadosDetalhamentoFinanceiro {
  valorTotal: number;    // compra inteira. Em "valor" vem só o deste mês
  numeroParcela: number;
  fixo: boolean;
}