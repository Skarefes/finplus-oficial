import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { DadosResumoFinanceiro } from '../models/dadosresumo-financeiro';

@Injectable({
  providedIn: 'root',
})
export class ResumoFinanceiroService {
  private apiUrl = 'http://localhost:8080/resumo';

  constructor(private http:HttpClient){}

  buscarResumo(){
    return this.http.get<DadosResumoFinanceiro>(
      `${this.apiUrl}/soma-tudo`
    );
  }
}
