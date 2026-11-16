import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { CurrencyPipe } from '@angular/common';
import { FinanceiroService } from '../../services/financeiro.service';
import { DadosDetalhamentoFinanceiro } from '../../models/dadosdetalhamento-financeiro';
import { ParcelasModal } from '../../components/parcelas-modal/parcelas-modal';
import { TransacaoForm } from '../../components/transacao-form/transacao-form';

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
  imports: [CurrencyPipe, ParcelasModal, TransacaoForm],
  templateUrl: './relatorios.html',
  styleUrl: './relatorios.css',
})
export class Relatorios implements OnInit {
  private financeiroService = inject(FinanceiroService);

  financeiros = signal<DadosDetalhamentoFinanceiro[]>([]);
  financeiroSelecionado = signal<DadosDetalhamentoFinanceiro | null>(null); // modal de parcelas
  formularioAberto = signal(false);
  financeiroEditando = signal<DadosDetalhamentoFinanceiro | null>(null);    // null = criando

  ngOnInit(): void {
    this.carregar();
  }

  carregar(): void {
    this.financeiroService.buscarFinanceiro().subscribe((lista) => this.financeiros.set(lista));
  }

  // mais recentes primeiro
  ganhos = computed(() =>
    this.financeiros().filter((f) => f.tipo === 'RECEITA').sort((a, b) => b.id - a.id),
  );
  despesas = computed(() =>
    this.financeiros().filter((f) => f.tipo === 'DESPESA').sort((a, b) => b.id - a.id),
  );

  totalReceita = computed(() => this.ganhos().reduce((soma, f) => soma + f.valor, 0));
  totalDespesa = computed(() => this.despesas().reduce((soma, f) => soma + f.valor, 0));
  saldo = computed(() => this.totalReceita() - this.totalDespesa());

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

  // ---------- ações ----------
  abrirNovo() {
    this.financeiroEditando.set(null);
    this.formularioAberto.set(true);
  }

  abrirEdicao(f: DadosDetalhamentoFinanceiro) {
    this.financeiroEditando.set(f);
    this.formularioAberto.set(true);
  }

  fecharFormulario() {
    this.formularioAberto.set(false);
    this.financeiroEditando.set(null);
  }

  aoSalvar() {
    this.fecharFormulario();
    this.carregar();
  }

  excluir(id: number) {
    if (!confirm('Deseja excluir esta transação?')) {
      return;
    }
    this.financeiroService.excluirFinanceiro(id).subscribe(() => this.carregar());
  }

  abrirParcelas(f: DadosDetalhamentoFinanceiro) {
    this.financeiroSelecionado.set(f);
  }

  fecharParcelas() {
    this.financeiroSelecionado.set(null);
  }
}