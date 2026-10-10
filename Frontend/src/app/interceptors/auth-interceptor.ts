import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { catchError, throwError } from 'rxjs';

// Rotas que nunca levam token (e cujo erro não significa "sessão vencida")
const ROTAS_PUBLICAS = ['/auth/', '/usuario/registrar'];

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const auth = inject(AuthService);
  const token = auth.getToken();

  // Login/cadastro nunca levam token: um token vencido guardado no
  // navegador faria o próprio login ser rejeitado pelo filtro JWT
  const rotaAuthPublica = ROTAS_PUBLICAS.some((rota) => req.url.includes(rota));


  // A requisição é imutável, então criamos uma cópia com o cabeçalho
  const requisicao = token && !rotaAuthPublica
    ? req.clone({ setHeaders: { Authorization: `Bearer ${token}` } })
    : req;

  return next(requisicao).pipe(catchError((erro: HttpErrorResponse) => {
    if(token && !rotaAuthPublica && (erro.status === 401 || erro.status === 403)){
      auth.logout();
    }
    return throwError(() => erro);
  }));
};
