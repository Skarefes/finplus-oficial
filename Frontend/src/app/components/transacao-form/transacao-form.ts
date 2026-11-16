import { Component, OnInit, inject, input, output, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Observable } from 'rxjs';
import { FinanceiroService } from '../../services/financeiro.service';
import { DadosDetalhamentoFinanceiro } from '../../models/dadosdetalhamento-financeiro';
import { DadosCadastroFinanceiro } from '../../models/dadoscadastro-financeiro';

@Component({
  selector: 'app-transacao-form',
  imports: [FormsModule],
  templateUrl: './transacao-form.html',
  styleUrl: './transacao-form.css',
})
export class TransacaoForm implements OnInit {
  private financeiroService = inject(FinanceiroService);

  // null = criando | preenchido = editando
  financeiro = input<DadosDetalhamentoFinanceiro | null>(null);
  salvo = output<void>();   // avisa o pai que salvou (ele recarrega a lista)
  fechar = output<void>();

  nome = '';
  valor = 0;
  descricao = '';
  tipo = 'DESPESA';
  categoria = 'OUTROS';
  formaPagamento = 'PIX';
  quantidadeParcelas = 1;

  salvando = signal(false);
  erro = signal('');

  get editando() {
    return this.financeiro() !== null;
  }

  // o back só aceita parcelar em CREDITO e BOLETO (enum FormaPagamento)
  get permiteParcelar() {
    return this.formaPagamento === 'CREDITO' || this.formaPagamento === 'BOLETO';
  }

  ngOnInit(): void {
    const f = this.financeiro();
    if (f) {
      this.nome = f.nome;
      this.valor = f.valor;
      this.descricao = f.descricao ?? '';
      this.tipo = f.tipo;
      this.categoria = f.categoria ?? 'OUTROS';
      this.formaPagamento = f.formaPagamento;
    }
  }

  salvar() {
    this.salvando.set(true);
    this.erro.set('');

    const dados: DadosCadastroFinanceiro = {
      nome: this.nome,
      valor: this.valor,
      descricao: this.descricao,
      tipo: this.tipo,
      categoria: this.categoria,
      formaPagamento: this.formaPagamento,
      // se trocou de crédito para PIX, não pode mandar 3 parcelas (o back daria erro 400)
      quantidadeParcelas: this.permiteParcelar ? this.quantidadeParcelas : 1,
    };

    const atual = this.financeiro();
    const requisicao: Observable<unknown> = atual
      ? this.financeiroService.alterarFinanceiro(atual.id, dados)
      : this.financeiroService.criarFinanceiro(dados);

    requisicao.subscribe({
      next: () => {
        this.salvando.set(false);
        this.salvo.emit();
      },
      error: (e) => {
        this.salvando.set(false);
        // o back devolve texto puro em alguns erros (ex.: forma de pagamento não permite parcelar)
        this.erro.set(typeof e.error === 'string' ? e.error : 'Não foi possível salvar. Tente novamente.');
      },
    });
  }
}