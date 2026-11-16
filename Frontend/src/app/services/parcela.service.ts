import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { DadosDetalhamentoParcela } from '../models/dados-detalhamento-parcela';

@Injectable({
  providedIn: 'root',
})
export class ParcelaService {

  private apiUrl = `${environment.apiUrl}/financeiro`;

  constructor(private http: HttpClient) {}

  buscarParcelas(idFinanceiro: number) {
    return this.http.get<DadosDetalhamentoParcela[]>(
      `${this.apiUrl}/${idFinanceiro}/parcelas`
    );
  }
}