// services/fixo.service.ts
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { DadosFixo, DadosCadastroFixo } from '../models/dados-fixo';

@Injectable({ providedIn: 'root' })
export class FixoService {
  private apiUrl = `${environment.apiUrl}/fixos`;

  constructor(private http: HttpClient) {}

  listar() {
    return this.http.get<DadosFixo[]>(this.apiUrl);
  }

  criar(fixo: DadosCadastroFixo) {
    return this.http.post<DadosFixo>(this.apiUrl, fixo);
  }

  alterar(id: number, fixo: DadosCadastroFixo) {
    return this.http.put<DadosFixo>(`${this.apiUrl}/${id}`, fixo);
  }

  excluir(id: number) {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}