export interface DadosDetalhamentoFinanceiro {
    id: number;
    nome: string;
    valor: number;
    descricao?: string;
    tipo: string;
    formaPagamento: string;
    quantidadeParcelas?: number;
}
