export interface DadosCadastroFinanceiro {
    nome: string;
    valor: number;
    descricao?: string;
    tipo: string;
    formaPagamento: string;
    quantidadeParcelas?: number;
}
