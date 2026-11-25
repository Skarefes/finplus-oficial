import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { CurrencyPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Observable } from 'rxjs';
import { FixoService } from '../../services/fixo.service';
import { DadosFixo, DadosCadastroFixo } from '../../models/dados-fixo';

const CATEGORIAS: Record<string, string> = {
  ALIMENTO: 'Alimentação',
  TRANSPORTE: 'Transporte',
  LAZER: 'Lazer',
  SAUDE: 'Saúde',
  OBJETO: 'Objeto',
  OUTROS: 'Outros',
};

@Component({
  selector: 'app-fixos',
  imports: [CurrencyPipe, FormsModule],
  templateUrl: './fixos.html',
  styleUrl: './fixos.css',
})
export class Fixos implements OnInit {
  private fixoService = inject(FixoService);

  fixos = signal<DadosFixo[]>([]);

  // maiores primeiro: a barra mais comprida fica no topo
  ganhos = computed(() => this.fixos().filter((f) => f.tipo === 'RECEITA').sort((a, b) => b.valor - a.valor));
  gastos = computed(() => this.fixos().filter((f) => f.tipo === 'DESPESA').sort((a, b) => b.valor - a.valor));

  totalGanhos = computed(() => this.ganhos().reduce((soma, f) => soma + f.valor, 0));
  totalGastos = computed(() => this.gastos().reduce((soma, f) => soma + f.valor, 0));
  sobra = computed(() => this.totalGanhos() - this.totalGastos());

  // base das barras: o maior entre ganhos e gastos, assim nenhuma barra passa de 100%
  base = computed(() => Math.max(this.totalGanhos(), this.totalGastos()));

  // quanto da renda fixa os gastos fixos consomem (pode passar de 100%)
  comprometido = computed(() =>
    this.totalGanhos() > 0 ? (this.totalGastos() / this.totalGanhos()) * 100 : 0,
  );
  comprometidoBarra = computed(() => Math.min(100, this.comprometido()));

  // ---------- formulário (modal) ----------
  formularioAberto = signal(false);
  editandoId = signal<number | null>(null); // null = criando
  salvando = signal(false);
  erro = signal('');

  nome = '';
  valor = 0;
  tipo = 'DESPESA';
  categoria = 'OUTROS';
  diaDoMes = 1;

  ngOnInit(): void {
    this.carregar();
  }

  carregar(): void {
    this.fixoService.listar().subscribe((lista) => this.fixos.set(lista));
  }

  largura(valor: number): number {
    const base = this.base();
    return base > 0 ? (valor / base) * 100 : 0;
  }

  nomeCategoria(chave: string): string {
    return CATEGORIAS[chave] ?? chave;
  }

  abrirNovo() {
    this.editandoId.set(null);
    this.nome = '';
    this.valor = 0;
    this.tipo = 'DESPESA';
    this.categoria = 'OUTROS';
    this.diaDoMes = 1;
    this.erro.set('');
    this.formularioAberto.set(true);
  }

  abrirEdicao(f: DadosFixo) {
    this.editandoId.set(f.id);
    this.nome = f.nome;
    this.valor = f.valor;
    this.tipo = f.tipo;
    this.categoria = f.categoria;
    this.diaDoMes = f.diaDoMes;
    this.erro.set('');
    this.formularioAberto.set(true);
  }

  fecharFormulario() {
    this.formularioAberto.set(false);
  }

  salvar() {
    this.salvando.set(true);
    this.erro.set('');

    const dados: DadosCadastroFixo = {
      nome: this.nome,
      valor: this.valor,
      tipo: this.tipo,
      categoria: this.categoria,
      diaDoMes: this.diaDoMes,
    };

    const id = this.editandoId();
    const requisicao: Observable<unknown> =
      id !== null ? this.fixoService.alterar(id, dados) : this.fixoService.criar(dados);

    requisicao.subscribe({
      next: () => {
        this.salvando.set(false);
        this.fecharFormulario();
        this.carregar();
      },
      error: (e) => {
        this.salvando.set(false);
        this.erro.set(typeof e.error === 'string' ? e.error : 'Não foi possível salvar. Tente novamente.');
      },
    });
  }

  excluir(f: DadosFixo) {
    const confirmou = confirm(
      `Excluir "${f.nome}"? Ele sai deste mês e dos próximos; os meses anteriores continuam no histórico.`,
    );
    if (!confirmou) {
      return;
    }
    this.fixoService.excluir(f.id).subscribe(() => this.carregar());
  }
}
