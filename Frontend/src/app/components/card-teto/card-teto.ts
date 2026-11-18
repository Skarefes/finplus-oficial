import { Component, OnInit, computed, inject, input, signal } from '@angular/core';
import { CurrencyPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ResumoFinanceiroService } from '../../services/resumo-financeiro.service';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-card-teto',
  imports: [CurrencyPipe, FormsModule, RouterLink],
  templateUrl: './card-teto.html',
  styleUrl: './card-teto.css',
  host: {'[class.compacto]':'compacto()'},
})
export class CardTeto implements OnInit {
  private resumoService = inject(ResumoFinanceiroService);

  // a página-pai passa os gastos do mês; o card compara com o limite
  gastos = input.required<number>();

  teto = signal<number | null>(null);
  editando = signal(false);
  tetoDigitado: number | null = null; // ligado ao input com ngModel

  restante = computed(() => {
    const t = this.teto();
    return t === null ? null : t - this.gastos();
  });
  estourou = computed(() => (this.restante() ?? 0) < 0);
  diferenca = computed(() => Math.abs(this.restante() ?? 0));

  // limitado a 100 para a barra não vazar da caixa
  percentual = computed(() => {
    const t = this.teto();
    return t ? Math.min(100, (this.gastos() / t) * 100) : 0;
  });

  ngOnInit(): void {
    this.resumoService.buscarTeto().subscribe((r) => this.teto.set(r.teto));
  }

  abrirEdicao() {
    this.tetoDigitado = this.teto();
    this.editando.set(true);
  }

  salvar() {
    // campo vazio = null = remove o limite
    this.resumoService.definirTeto(this.tetoDigitado).subscribe((r) => {
      this.teto.set(r.teto);
      this.editando.set(false);
    });
  }

  compacto = input(false);
}
