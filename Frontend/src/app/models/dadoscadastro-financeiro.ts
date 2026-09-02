export interface DadosCadastroFinanceiro {
    id: number;
    nome: string;
    valor: number;
    descricao?: string;
    tipo: string;
    formaPagamento: string;
    quantidadeParcelas?: number;
}
