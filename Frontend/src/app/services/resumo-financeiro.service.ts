import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { DadosResumoFinanceiro } from '../models/dadosresumo-financeiro';
import { environment } from '../../environments/environment';
import { DadosTeto } from '../models/dados-teto';

//Diz ao angular que esse service pode ser utilizado em toda aplicação
@Injectable({
  providedIn: 'root',
})
export class ResumoFinanceiroService {
  private apiUrl = `${environment.apiUrl}/resumo`;

  constructor(private http: HttpClient){}

  buscarResumo(){
    return this.http.get<DadosResumoFinanceiro>(
      `${this.apiUrl}/soma-tudo`
    );
  }

  buscarTeto(){
    return this.http.get<DadosTeto>(`${this.apiUrl}/teto`);
  }

  definirTeto(teto: number | null){
    return this.http.put<DadosTeto>(`${this.apiUrl}/teto`, {teto});
  }
}
