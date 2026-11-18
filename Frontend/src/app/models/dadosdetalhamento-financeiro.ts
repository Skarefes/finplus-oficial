export interface DadosDetalhamentoFinanceiro {
    id: number;
    nome: string;
    valor: number;
    descricao?: string;
    data: string;
    tipo: string;
    formaPagamento: string;
    categoria: string;
    quantidadeParcelas?: number;
}
