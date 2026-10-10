import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-configuracoes',
  imports: [],
  templateUrl: './configuracoes.html',
  styleUrl: './configuracoes.css',
})
export class Configuracoes {
  erro = '';

  constructor(private auth: AuthService) {}

  excluirConta() {
    const confirmou = confirm(
      'Isso apagará sua conta e TODAS as suas transações. Essa ação não pode ser desfeita. Continuar?',
    );
    if (!confirmou) {
      return;
    }

    this.auth.excluirConta().subscribe({
      next: () => this.auth.logout(),
      error: () => (this.erro = 'Não foi possível excluir a conta. Tente novamente.'),
    });
  }
}