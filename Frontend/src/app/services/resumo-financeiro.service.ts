import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { DadosResumoFinanceiro } from '../models/dadosresumo-financeiro';

//Diz ao angular que esse service pode ser utilizado em toda aplicação
@Injectable({
  providedIn: 'root',
})
export class ResumoFinanceiroService {
  private apiUrl = 'http://localhost:8080/resumo';

  constructor(private http: HttpClient){}

  buscarResumo(){
    return this.http.get<DadosResumoFinanceiro>(
      `${this.apiUrl}/soma-tudo`
    );
  }
}
