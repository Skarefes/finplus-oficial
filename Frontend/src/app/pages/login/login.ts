import { Component } from '@angular/core';
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
  email = '';
  senha = '';
  modoCadastro = false; // false = entrar | true = criar conta
  erro = '';
  carregando = false;

  constructor(
    private auth: AuthService,
    private router: Router,
  ) {}

  alternarModo() {
    this.modoCadastro = !this.modoCadastro;
    this.erro = '';
  }

  enviar() {
    this.erro = '';
    this.carregando = true;

    if (this.modoCadastro) {
      // Cria a conta e, se der certo, já entra
      this.auth.cadastrar({ email: this.email, senha: this.senha }).subscribe({
        next: () => this.entrar(),
        error: () => {
          this.erro = 'Não foi possível criar a conta. Tente outro login.';
          this.carregando = false;
        },
      });
    } else {
      this.entrar();
    }
  }

  private entrar() {
    this.auth.login({ email: this.email, senha: this.senha }).subscribe({
      next: () => this.router.navigate(['/']), // vai para o painel
      error: () => {
        this.erro = 'Login ou senha inválidos.';
        this.carregando = false;
      },
    });
  }
}
