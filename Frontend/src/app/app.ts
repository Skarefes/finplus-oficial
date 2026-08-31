import { Component, OnInit, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { ResumoFinanceiroService } from './services/resumo-financeiro.service';
import { CurrencyPipe } from '@angular/common';


@Component({
  selector: 'app-root',
  imports: [RouterOutlet, CurrencyPipe],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App implements OnInit{
  protected readonly title = signal('frontend');

  totalReceita = 0;
  totalDespesa = 0;

  constructor(private resumoFinanceiro: ResumoFinanceiroService) {}

  ngOnInit(): void {
    this.resumoFinanceiro.buscarResumo().subscribe(resumo => {

      console.log('Resumo: ', resumo)
      this.totalReceita = resumo.totalReceita;
      this.totalDespesa = resumo.totalDespesa;
    });
  }
}
