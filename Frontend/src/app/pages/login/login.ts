import { Component, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  imports: [FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  username = '';
  email = '';
  senha = '';
  modoCadastro = false;

  // signals: o Angular atualiza a tela sozinho quando o valor muda
  erro = signal('');
  carregando = signal(false);
  demorando = signal(false);

  private temporizador: any;

  constructor(
    private auth: AuthService,
    private router: Router,
  ) {}

  alternarModo() {
    this.modoCadastro = !this.modoCadastro;
    this.erro.set('');
    this.username = '';
    this.senha = '';
  }

  enviar() {
    this.erro.set('');
    this.iniciarEspera();

    if (this.modoCadastro) {
      this.auth.cadastrar({
        username: this.username,
         email: this.email, 
         senha: this.senha }).subscribe({
        next: () => this.entrar(),
        error: () => {
          this.erro.set('Não foi possível criar a conta. Tente outro login.');
          this.pararEspera();
        },
      });
    } else {
      this.entrar();
    }
  }

  private entrar() {
    this.auth.login({ email: this.email, senha: this.senha }).subscribe({
      next: () => {
        this.pararEspera();
        this.router.navigate(['/']);
      },
      error: (e) => {
        // status 0 = nem chegou resposta (rede, servidor fora do ar)
        this.erro.set(
          e.status === 0
            ? 'Sem conexão com o servidor. Tente novamente.'
            : 'Login ou senha inválidos.',
        );
        this.pararEspera();
      },
    });
  }

  private iniciarEspera() {
    this.carregando.set(true);
    this.demorando.set(false);
    clearTimeout(this.temporizador);
    this.temporizador = setTimeout(() => this.demorando.set(true), 4000);
  }

  private pararEspera() {
    clearTimeout(this.temporizador);
    this.carregando.set(false);
    this.demorando.set(false);
  }
}