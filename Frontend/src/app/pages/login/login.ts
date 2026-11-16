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
  mensagemEspera = signal("Conectando ao servidor...")

  private temporizadores: any[] = [];

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
    this.mensagemEspera.set('Conectando ao servidor...');
    this.temporizadores.forEach(clearTimeout);
    this.temporizadores = [
      setTimeout(() => this.mensagemEspera.set('O servidor esta acordando, pode levar um tempo.., espere por favor'), 4000),
      setTimeout(() => this.mensagemEspera.set("Quase lá! Só mais um instante..."), 30000),
    ];
  }

  private pararEspera() {
    this.temporizadores.forEach(clearTimeout);
    this.carregando.set(false);
  }
}