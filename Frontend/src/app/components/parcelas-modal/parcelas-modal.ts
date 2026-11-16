import { Component, inject, input, signal, output } from '@angular/core';
import { CurrencyPipe } from '@angular/common';
import { ParcelaService } from '../../services/parcela.service';
import { DadosDetalhamentoFinanceiro } from '../../models/dadosdetalhamento-financeiro';
import { DadosDetalhamentoParcela } from '../../models/dados-detalhamento-parcela';

@Component({
  selector: 'app-parcelas-modal',
  imports: [CurrencyPipe],
  templateUrl: './parcelas-modal.html',
  styleUrl: './parcelas-modal.css',
})
export class ParcelasModal {
  private parcelaService = inject(ParcelaService);

  //input() o pai passa o financeiro, required obrigatório
  financeiro = input.required<DadosDetalhamentoFinanceiro>();
  //output() o filho avisa pro pai que quer fechar
  fechar = output<void>();

  parcelas = signal<DadosDetalhamentoParcela[]>([]);
  carregando = signal(true);
  erro = signal(false);

  ngOnInit(): void {
    this.parcelaService.buscarParcelas(this.financeiro().id).subscribe({
      next: (dados) => {
        this.parcelas.set(dados);
        this.carregando.set(false);
      },
      error: () => {
        this.erro.set(true);
        this.carregando.set(false);
      }
    })
  }

}


