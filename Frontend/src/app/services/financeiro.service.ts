import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { DadosCadastroFinanceiro } from '../models/dadoscadastro-financeiro';
import { DadosDetalhamentoFinanceiro } from '../models/dadosdetalhamento-financeiro';

@Injectable({
  providedIn: 'root',
})
export class FinanceiroService {
  private apiUrl = 'http://localhost:8080/financeiro'

  constructor(private http: HttpClient){}

  buscarFinanceiro(){
    return this.http.get<DadosDetalhamentoFinanceiro[]>(
      `${this.apiUrl}/listar-tudo`
    );

  }

  //Receba um objeto DadosCadastroFinanceiro e envie esse objeto para a API
  criarFinanceiro(financeiro: DadosCadastroFinanceiro){
    return this.http.post(`${this.apiUrl}/registrar`, financeiro);
  }
}
