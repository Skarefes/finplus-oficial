import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { CurrencyPipe } from '@angular/common';
import { RouterLink } from '@angular/router';

import { ResumoFinanceiroService } from '../../services/resumo-financeiro.service';
import { FinanceiroService } from '../../services/financeiro.service';
import { AuthService } from '../../services/auth.service';
import { DadosDetalhamentoFinanceiro } from '../../models/dadosdetalhamento-financeiro';
import { TransacaoForm } from '../../components/transacao-form/transacao-form';
import { ParcelasModal } from '../../components/parcelas-modal/parcelas-modal';
import { SeletorMes } from '../../components/seletor-mes/seletor-mes';
import { CardTeto } from '../../components/card-teto/card-teto';
import { PeriodoService } from '../../services/periodo.service';

@Component({
  selector: 'app-painel',
  imports: [CurrencyPipe, RouterLink, TransacaoForm, ParcelasModal, SeletorMes, CardTeto],
  templateUrl: './painel.html',
  styleUrl: './painel.css',
})
export class Painel implements OnInit {
  private resumoFinanceiro = inject(ResumoFinanceiroService);
  private financeiroService = inject(FinanceiroService);
  private periodo = inject(PeriodoService)
  auth = inject(AuthService);

  totalReceita = signal(0);
  totalDespesa = signal(0);
  financeiros = signal<DadosDetalhamentoFinanceiro[]>([]);

  formularioAberto = signal(false);
  financeiroEditando = signal<DadosDetalhamentoFinanceiro | null>(null); // null = criando
  financeiroSelecionado = signal<DadosDetalhamentoFinanceiro | null>(null); // modal de parcelas
  
  ganhosRecentes = computed(() =>
    this.financeiros()
      .filter((f) => f.tipo === 'RECEITA')
      .sort((a, b) => b.id - a.id)
      .slice(0, 5),
  );

  gastosRecentes = computed(() =>
    this.financeiros()
      .filter((f) => f.tipo === 'DESPESA')
      .sort((a, b) => b.id - a.id)
      .slice(0, 5),
  );

  receitaMes = computed(() =>
    this.financeiros().filter((f) => f.tipo === 'RECEITA').reduce((soma, f) => soma + f.valor, 0),
  );
  despesaMes = computed(() =>
    this.financeiros().filter((f) => f.tipo === 'DESPESA').reduce((soma, f) => soma + f.valor, 0),
  );

  primeiroNome = computed(() => {
    const nome = this.auth.nomeUsuario().trim().split(' ')[0];
    return nome.charAt(0).toUpperCase() + nome.slice(1);
  });

  ngOnInit(): void {
    this.carregar();
  }

  carregar(): void {
    this.resumoFinanceiro.buscarResumo().subscribe((resumo) => {
      this.totalReceita.set(resumo.totalReceita);
      this.totalDespesa.set(resumo.totalDespesa);
    });

    //this.financeiroService.buscarFinanceiro().subscribe((lista) => this.financeiros.set(lista));

    this.financeiroService
      .buscarFinanceiroPorMes(this.periodo.mes(), this.periodo.ano())
      .subscribe((lista) => this.financeiros.set(lista));
  }

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
    this.carregar(); // recarrega lista e totais
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