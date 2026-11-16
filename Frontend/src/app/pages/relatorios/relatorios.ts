import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { CurrencyPipe } from '@angular/common';
import { FinanceiroService } from '../../services/financeiro.service';
import { DadosDetalhamentoFinanceiro } from '../../models/dadosdetalhamento-financeiro';
import { ParcelasModal } from '../../components/parcelas-modal/parcelas-modal';

// nome amigável + cor de cada categoria num lugar só
const CATEGORIAS: Record<string, { nome: string; cor: string }> = {
  ALIMENTO: { nome: 'Alimentação', cor: '#198b69' },
  TRANSPORTE: { nome: 'Transporte', cor: '#4b9bc0' },
  LAZER: { nome: 'Lazer', cor: '#e0a43a' },
  SAUDE: { nome: 'Saúde', cor: '#d95e59' },
  OBJETO: { nome: 'Objeto', cor: '#7a62c9' },
  OUTROS: { nome: 'Outros', cor: '#9aa8b0' },
};

@Component({
  selector: 'app-relatorios',
  imports: [CurrencyPipe, ParcelasModal],
  templateUrl: './relatorios.html',
  styleUrl: './relatorios.css',
})
export class Relatorios implements OnInit {
  private financeiroService = inject(FinanceiroService);

  financeiros = signal<DadosDetalhamentoFinanceiro[]>([]);
  financeiroSelecionado = signal<DadosDetalhamentoFinanceiro | null>(null);

  ngOnInit(): void {
    this.financeiroService.buscarFinanceiro().subscribe((lista) => this.financeiros.set(lista));
  }

  despesas = computed(() => this.financeiros().filter((f) => f.tipo === 'DESPESA'));

  totalReceita = computed(() =>
    this.financeiros()
      .filter((f) => f.tipo === 'RECEITA')
      .reduce((soma, f) => soma + f.valor, 0),
  );
  totalDespesa = computed(() => this.despesas().reduce((soma, f) => soma + f.valor, 0));
  saldo = computed(() => this.totalReceita() - this.totalDespesa());

  // soma os gastos por categoria e calcula a % de cada uma
  gastosPorCategoria = computed(() => {
    const total = this.totalDespesa();
    const somas = new Map<string, number>();

    for (const d of this.despesas()) {
      const chave = d.categoria ?? 'OUTROS';
      somas.set(chave, (somas.get(chave) ?? 0) + d.valor);
    }

    return [...somas.entries()]
      .map(([chave, valor]) => ({
        nome: CATEGORIAS[chave]?.nome ?? chave,
        cor: CATEGORIAS[chave]?.cor ?? '#9aa8b0',
        valor,
        percentual: total > 0 ? (valor / total) * 100 : 0,
      }))
      .sort((a, b) => b.valor - a.valor);
  });

  // monta: conic-gradient(cor1 0% 40%, cor2 40% 70%, ...)
  gradiente = computed(() => {
    let inicio = 0;
    const partes = this.gastosPorCategoria().map((c) => {
      const fim = inicio + c.percentual;
      const trecho = `${c.cor} ${inicio}% ${fim}%`;
      inicio = fim;
      return trecho;
    });
    return `conic-gradient(${partes.join(', ')})`;
  });

  nomeCategoria(chave?: string) {
    return CATEGORIAS[chave ?? 'OUTROS']?.nome ?? chave;
  }

  abrirParcelas(f: DadosDetalhamentoFinanceiro) {
    this.financeiroSelecionado.set(f);
  }

  fecharParcelas() {
    this.financeiroSelecionado.set(null);
  }
}
