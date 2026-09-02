import { Component, OnInit, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { ResumoFinanceiroService } from './services/resumo-financeiro.service';
import { CurrencyPipe } from '@angular/common';
import { FinanceiroService } from './services/financeiro.service';
import { DadosDetalhamentoFinanceiro } from './models/dadosdetalhamento-financeiro';


@Component({
  selector: 'app-root',
  imports: [RouterOutlet, CurrencyPipe],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App implements OnInit{
  protected readonly title = signal('frontend');

  //Dados da tela
  totalReceita = 0;
  totalDespesa = 0;
  financeiros: DadosDetalhamentoFinanceiro[] = [];

  //Dependencias da classe
  constructor(private resumoFinanceiro: ResumoFinanceiroService ,
    private financeiroService: FinanceiroService
  ) {}

  
  //Quando o componente iniciar
  ngOnInit(): void {
    //busque o resumo
    this.resumoFinanceiro.buscarResumo().subscribe(resumo => {

      //coloque os valores recebidos
      console.log('Resumo: ', resumo)
      this.totalReceita = resumo.totalReceita;
      this.totalDespesa = resumo.totalDespesa;
    });

    //busque todos os financeiros
    this.financeiroService.buscarFinanceiro().subscribe(financeiros => {
      //coloque os resultados na lista
      this.financeiros = financeiros;
    })
  }

  mostrarFormulario = false;

  abrirFormulario(){
    this.mostrarFormulario = true;
  }

  fecharFormulario(){
    this.mostrarFormulario = false;
  }

}
