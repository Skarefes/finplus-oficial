import { HttpClient } from '@angular/common/http';
import { Injectable, signal } from '@angular/core';
import { Router } from '@angular/router';
import { tap } from 'rxjs';
import { DadosLogin, DadosToken } from '../models/dados-login';
import { DadosCadastroUsuario } from '../models/dados-cadastro-usuario';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private apiUrl = environment.apiUrl;
  private readonly CHAVE_TOKEN = 'finplus_token';
  private readonly CHAVE_NOME = 'finplus_nome'; // NOVO

  // true se já existe um token salvo (assim o usuário continua logado ao dar F5)
  logado = signal(this.estaLogado());

  // NOVO: agora é um signal que guarda o nome salvo (sobrevive ao F5 pelo localStorage)
  nomeUsuario = signal<string>(localStorage.getItem(this.CHAVE_NOME) ?? '');

  constructor(private http: HttpClient,
    private router: Router,) {}

  login(dados: DadosLogin){
    return this.http.post<DadosToken>(`${this.apiUrl}/auth/login`, dados).pipe(
        //tap = faça algo com a resposta sem alterar nada
        tap((resposta) => {
            localStorage.setItem(this.CHAVE_TOKEN, resposta.token);
            localStorage.setItem(this.CHAVE_NOME, resposta.username); // NOVO
            this.nomeUsuario.set(resposta.username);                  // NOVO
            this.logado.set(true);
        })
    )
  }

  cadastrar(dados: DadosCadastroUsuario){
    return this.http.post(`${this.apiUrl}/usuario/registrar`, dados);
  }

  excluirConta(){
    return this.http.delete(`${this.apiUrl}/usuario/me`);
  }

  logout() {
    localStorage.removeItem(this.CHAVE_TOKEN);
    localStorage.removeItem(this.CHAVE_NOME); // NOVO
    this.nomeUsuario.set('');                 // NOVO
    this.logado.set(false);
    this.router.navigate(['/login']);
  }

  getToken(): string | null {
    return localStorage.getItem(this.CHAVE_TOKEN);
  }

  estaLogado(): boolean {
    const payload = this.lerPayload();
    if (!payload) return false;
    return payload.exp * 1000 > Date.now();
  }

  // centraliza a leitura do payload (antes estava dentro do estaLogado)
  private lerPayload(): any | null {
    const token = this.getToken();
    if (!token) return null;

    try {
      // JWT usa base64url: troca - e _ pelos caracteres do base64 normal
      const base64 = token.split('.')[1].replace(/-/g, '+').replace(/_/g, '/');
      // o atob sozinho estraga acentos (José vira JosÃ©); isto decodifica como UTF-8
      const bytes = Uint8Array.from(atob(base64), (c) => c.charCodeAt(0));
      return JSON.parse(new TextDecoder().decode(bytes));
    } catch {
      return null;
    }
  }
}