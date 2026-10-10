import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CurrencyPipe } from '@angular/common';

import { ResumoFinanceiroService } from '../../services/resumo-financeiro.service';
import { FinanceiroService } from '../../services/financeiro.service';

import { DadosDetalhamentoFinanceiro } from '../../models/dadosdetalhamento-financeiro';
import { DadosCadastroFinanceiro } from '../../models/dadoscadastro-financeiro';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-painel',
  imports: [CurrencyPipe, FormsModule],
  templateUrl: './painel.html',
  styleUrl: './painel.css',
})
export class Painel implements OnInit {

  // A lógica financeira antiga será colocada aqui.
  // Preserve os atributos e métodos do seu App antigo.

  //Dados do formulário
  nome = '';
  valor = 0;
  descricao = '';
  tipo = 'DESPESA';
  formaPagamento = 'PIX';
  quantidadeParcelas = 1;


  constructor(
    private resumoFinanceiro: ResumoFinanceiroService,
    private financeiroService: FinanceiroService,
  ) {}

  ngOnInit(): void {
    this.atualizarResumo();

    this.financeiroService.buscarFinanceiro().subscribe((financeiros) => {
      this.financeiros.set(financeiros);
    });
  }

  totalReceita = signal(0);
  totalDespesa = signal(0);
  financeiros = signal<DadosDetalhamentoFinanceiro[]>([]);

  //funcionalidades

  auth = inject(AuthService);


  // pega a 1ª palavra do nome e deixa a 1ª letra maiúscula: "roger silva" -> "Roger"
  primeiroNome = computed(() => {
  const nome = this.auth.nomeUsuario().trim().split(' ')[0];
  return nome.charAt(0).toUpperCase() + nome.slice(1);
  });

  inicial = computed(() => this.primeiroNome().charAt(0));

  //busque o resumo e podemos atualizar este resumo
  atualizarResumo(): void {
    this.resumoFinanceiro.buscarResumo().subscribe((resumo) => {
      this.totalReceita.set(resumo.totalReceita);
      this.totalDespesa.set(resumo.totalDespesa);
    });
  }

  //o financeiro esta editando agora? ajudando o angular entender se estou editando ou criando
  financeiroEditando?:DadosDetalhamentoFinanceiro;

  mostrarFormulario = false;

  abrirFormulario() {
    this.limparFormulario()
    this.mostrarFormulario = true;
  }

  fecharFormulario() {
    this.mostrarFormulario = false;
  }

  //Dados Formulario
  mostrarDados() {
    console.log('Nome: ', this.nome);
    console.log('Valor: ', this.valor);
    console.log('Descrição: ', this.descricao);
    console.log('Tipo: ', this.tipo);
    console.log('Forma de Pagamento: ', this.formaPagamento);
    console.log('Parcelas: ', this.quantidadeParcelas);
  }

  //como tenho apenas um formulario, tanto para criar e editar tenho que ter um jeito de saber diferenciar os dois, e não ter problemas
  salvarFinanceiro(){
    if(this.financeiroEditando){
      this.salvarEdicao();
    } else {
      this.criarFinanceiro();
    }
  }

  //crio o novo financeiro
  criarFinanceiro() {
    const novoFinanceiro: DadosCadastroFinanceiro = {
      nome: this.nome,
      valor: this.valor,
      descricao: this.descricao,
      tipo: this.tipo,
      formaPagamento: this.formaPagamento,
      quantidadeParcelas: this.quantidadeParcelas,
    };

    //atualiza os dados  da lista de ganhos e gastos
    this.financeiroService.criarFinanceiro(novoFinanceiro).subscribe(() => {
      console.log('Financeiro Criado!')
      //atualiza a lista
      this.financeiroService.buscarFinanceiro().subscribe((financeiros) => {
        console.log('Lista recebida: ', financeiros)
        this.financeiros.set(financeiros);
        //atualiza os ganhos e gastos
        this.atualizarResumo();
        this.mostrarFormulario = false;
        this.limparFormulario();
      });
    });

    
  }

  editarFormulario(financeiro: DadosDetalhamentoFinanceiro) {
    this.financeiroEditando = financeiro;

    this.nome = financeiro.nome;
    this.valor = financeiro.valor;
    this.descricao = financeiro.descricao ?? '';
    this.tipo = financeiro.tipo;
    this.formaPagamento = financeiro.formaPagamento;
    this.quantidadeParcelas = financeiro.quantidadeParcelas ?? 1;

    this.mostrarFormulario = true;
  }

  //metodo que salva a edição que eu fiz em formulario
  salvarEdicao() {
    if (!this.financeiroEditando) {
      return;
    }

    const financeiroAtualizado: DadosCadastroFinanceiro = {
      nome: this.nome,
      valor: this.valor,
      descricao: this.descricao,
      tipo: this.tipo,
      formaPagamento: this.formaPagamento,
      quantidadeParcelas: this.quantidadeParcelas,
    };

    this.financeiroService
      .alterarFinanceiro(this.financeiroEditando.id, financeiroAtualizado).subscribe((resposta) => {
        console.log('Financeiro alterado:', resposta);

        //busca novamente os dados atualizados no banco
        this.financeiroService.buscarFinanceiro().subscribe((financeiros) => {
          this.financeiros.set(financeiros);
            //atualiza os totais
          this.atualizarResumo();

            //fecha os formularios
          this.mostrarFormulario = false

            //limpa o financeiro antigo
          this.financeiroEditando = undefined;

            //limpa o formulario 
          this.limparFormulario();
        });
      });
  }

  excluirFinanceiro(id:number){
    if(!confirm('Deseja excluir os dados?')){
      return;
    }

    this.financeiroService.excluirFinanceiro(id).subscribe(() => {
      console.log('Financeiro excluido')

      //atualiza a lista
      this.financeiroService.buscarFinanceiro().subscribe((financeiros) => {
        this.financeiros.set(financeiros);
        //atualiza tudo
        this.atualizarResumo();
      })
    })
  }

  limparFormulario(){
    this.nome= '';
    this.valor = 0;
    this.descricao = '';
    this.tipo = 'DESPESA';
    this.formaPagamento = 'PIX';
    this.quantidadeParcelas = 1;

    this.financeiroEditando = undefined;
  }

}

