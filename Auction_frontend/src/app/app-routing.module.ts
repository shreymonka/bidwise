import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LandingPageComponent } from './core/components/landing-page/landing-page.component';
import { LoginPageComponent } from './core/components/login-page/login-page.component';
import { SignupPageComponent } from './core/components/signup-page/signup-page.component';

const routes: Routes = [
  // {
  //   path:'account', 
  //   loadChildren:()=>
  //     import('./modules/account.module').then((m)=>m.AccountModule),
  // },
  {path:'', redirectTo:'landingpage',pathMatch:'full'},
  {path:'landingpage',component:LandingPageComponent},
  {path:'login',component:LoginPageComponent},
  {path:'signup',component:SignupPageComponent},


];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
