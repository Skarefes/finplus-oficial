import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { CurrencyPipe } from '@angular/common';
import { FinanceiroService } from '../../services/financeiro.service';
import { DadosDetalhamentoFinanceiro } from '../../models/dadosdetalhamento-financeiro';
import { ParcelasModal } from '../../components/parcelas-modal/parcelas-modal';
import { TransacaoForm } from '../../components/transacao-form/transacao-form';
import { PeriodoService } from '../../services/periodo.service';
import { SeletorMes } from '../../components/seletor-mes/seletor-mes';
import { CardTeto } from '../../components/card-teto/card-teto';
import { DadosLancamentoMensal } from '../../models/dados-lancamento-mensal';

function chaveData(data: string): string {
  return data.split('/').reverse().join('');
}

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
  imports: [CurrencyPipe, ParcelasModal, TransacaoForm, SeletorMes, CardTeto],
  templateUrl: './relatorios.html',
  styleUrl: './relatorios.css',
})

export class Relatorios implements OnInit {
  private financeiroService = inject(FinanceiroService);
  private periodo = inject(PeriodoService)

  financeiros = signal<DadosLancamentoMensal[]>([]);
  financeiroSelecionado = signal<DadosDetalhamentoFinanceiro | null>(null); // modal de parcelas
  formularioAberto = signal(false);
  financeiroEditando = signal<DadosDetalhamentoFinanceiro | null>(null);    // null = criando
  //mes e ano estado do filtro, os outros reagem a ele
  teto = signal<number | null>(null);
  editandoTeto = signal(false);
  tetoDigitado: number | null = null; //ligado ao input ngmodel
  historicoAberto = signal(false);
  historico = signal<DadosDetalhamentoFinanceiro[]>([]);
  filtroHistorico = signal<'TODOS' | 'RECEITA' | 'DESPESA'>('TODOS');
  limiteHistorico = signal(10);


  ngOnInit(): void {
    this.carregar();
  }

  carregar(): void {
  this.financeiroService
    .buscarFinanceiroPorMes(this.periodo.mes(), this.periodo.ano())
    .subscribe((lista) => this.financeiros.set(lista));
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
    this.atualizarTela();
  }

  excluir(id: number) {
    if (!confirm('Deseja excluir esta transação? Se for parcelada, todas as parcelas serão excluídas.')) {
      return;
    }
    this.financeiroService.excluirFinanceiro(id).subscribe(() => this.atualizarTela());
  }

  abrirParcelas(f: DadosDetalhamentoFinanceiro) {
    // no modal, "Total" deve ser a compra inteira, e não só a parcela deste mês.
    // as linhas do histórico não têm valorTotal, então caem no próprio valor
    const total = (f as Partial<DadosLancamentoMensal>).valorTotal ?? f.valor;
    this.financeiroSelecionado.set({ ...f, valor: total });
  }

  fecharParcelas() {
    this.financeiroSelecionado.set(null);
  }

  private atualizarTela(){
    this.carregar();
    if(this.historicoAberto()){
      this.carregarHistorico()
    }
  }

  //-------historico -------
  historicoFiltrado = computed(() => {
  const filtro = this.filtroHistorico();
  return this.historico()
    .filter((f) => filtro === 'TODOS' || f.tipo === filtro)
    // mais recente primeiro; se a data empatar, desempata pelo id
    .sort((a, b) => chaveData(b.data).localeCompare(chaveData(a.data)) || b.id - a.id);
  });

  historicoVisivel = computed(() => this.historicoFiltrado().slice(0, this.limiteHistorico()));
  restantes = computed(() => this.historicoFiltrado().length - this.historicoVisivel().length);

  alternarHistorico() {
    this.historicoAberto.update((v) => !v);
    // só busca na primeira vez que abre; fechado, não custa nada
    if (this.historicoAberto() && this.historico().length === 0) {
      this.carregarHistorico();
    }
  }

  carregarHistorico() {
    this.financeiroService.buscarFinanceiro().subscribe((lista) => this.historico.set(lista));
  }

  filtrar(tipo: 'TODOS' | 'RECEITA' | 'DESPESA') {
    this.filtroHistorico.set(tipo);
    this.limiteHistorico.set(10); // volta para a primeira página
  }

  mostrarMais() {
    this.limiteHistorico.update((l) => l + 10);
  }

  nomeCategoria(chave: string) {
    return CATEGORIAS[chave]?.nome ?? chave;
  }

}