import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { inject } from '@angular/core';

export const authGuard: CanActivateFn = () => {

  const auth = inject(AuthService);
  const router = inject(Router);

  //Caso o usuario nao tiver logado, ele volta pro url de login
  if(auth.estaLogado()){
    return true;
  }
  return router.createUrlTree(['/login']);
};
