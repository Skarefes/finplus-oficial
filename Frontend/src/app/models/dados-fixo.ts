// models/dados-fixo.ts
export interface DadosFixo {
  id: number;
  nome: string;
  valor: number;
  tipo: string;
  categoria: string;
  diaDoMes: number;
}

export type DadosCadastroFixo = Omit<DadosFixo, 'id'>; // tudo, menos o id