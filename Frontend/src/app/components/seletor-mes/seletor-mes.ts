import { Component, computed, inject, output, signal } from '@angular/core';
import { PeriodoService } from '../../services/periodo.service';

@Component({
  selector: 'app-seletor-mes',
  templateUrl: './seletor-mes.html',
  styleUrl: './seletor-mes.css',
})
export class SeletorMes {
  periodo = inject(PeriodoService);
  mudou = output<void>(); // avisa a página para recarregar

  meses = ['Jan', 'Fev', 'Mar', 'Abr', 'Mai', 'Jun', 'Jul', 'Ago', 'Set', 'Out', 'Nov', 'Dez'];
  aberto = signal(false);
  // o ano do painel é independente: dá para navegar de ano sem mudar o filtro
  anoExibido = signal(this.periodo.ano());

  titulo = computed(() => {
    const nome = new Date(this.periodo.ano(), this.periodo.mes() - 1, 1)
      .toLocaleDateString('pt-BR', { month: 'long' });
    return nome.charAt(0).toUpperCase() + nome.slice(1) + ' ' + this.periodo.ano();
  });

  alternar() {
    this.anoExibido.set(this.periodo.ano()); // ao abrir, começa no ano do filtro
    this.aberto.update((v) => !v);
  }

  mudarAno(delta: number) {
    this.anoExibido.update((a) => a + delta);
  }

  escolher(indice: number) {
    this.periodo.definir(indice + 1, this.anoExibido()); // índice 0 = janeiro
    this.aberto.set(false);
    this.mudou.emit();
  }

  deslocar(delta: number) {
    this.periodo.deslocar(delta);
    this.mudou.emit();
  }
}
