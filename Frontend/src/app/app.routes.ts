import { Routes } from '@angular/router';
import { Login } from './pages/login/login'; 
import { Painel } from './pages/painel/painel'; 
import { Configuracoes } from './pages/configuracoes/configuracoes'; 
import { authGuard } from './guards/auth-guard';

export const routes: Routes = [

    { path: 'login', component: Login }, 
    { path: '', component: Painel, canActivate: [authGuard] }, 
    { path: 'configuracoes', component: Configuracoes, canActivate: [authGuard] },
    { path: '**', redirectTo: '' }


    //{
        //path: 'login',
        //loadComponent: () => 
            //import('./pages/login/login').then(m => m.Login)
    //},
    //{
        //path: 'painel', 
        //loadComponent: () => import('./pages/painel/painel').then(m => m.Painel)
    //},
    //{
        //path: 'configuracoes',
        //loadComponent: () => import('./pages/configuracoes/configuracoes').then(m => m.Configuracoes)
    //},
    //{
        //path: '',
        //redirectTo: 'painel',
        //pathMatch: 'full'
    //},
    //{
        //path: '**',
        //redirectTo: 'login'
    //}
];
