// services/periodo.service.ts
import { Injectable, signal } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class PeriodoService {
  mes = signal(new Date().getMonth() + 1); // getMonth() vai de 0 a 11, por isso o +1
  ano = signal(new Date().getFullYear());

  definir(mes: number, ano: number) {
    this.mes.set(mes);
    this.ano.set(ano);
  }

  deslocar(delta: number) {
    // o Date resolve a virada de ano: mês 0 - 1 vira dezembro do ano anterior
    const d = new Date(this.ano(), this.mes() - 1 + delta, 1);
    this.definir(d.getMonth() + 1, d.getFullYear());
  }
}