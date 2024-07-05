import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LandingPageComponent } from './core/components/landing-page/landing-page.component';
import { LoginPageComponent } from './core/components/login-page/login-page.component';
import { SignupPageComponent } from './core/components/signup-page/signup-page.component';
import { AboutUsComponent } from './core/components/about-us/about-us.component'; 
import { PostLoginLandingPageComponent } from './core/components/post-login-landing-page/post-login-landing-page.component';
import { LoginServiceService } from './core/services/login-service/login-service.service';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { ForgetPasswordComponent } from './core/components/forget-password/forget-password.component';
import { ResetPasswordComponent } from './core/components/reset-password/reset-password.component';

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
  {path: 'about-us', component: AboutUsComponent},
  {path: 'postLogin', component: PostLoginLandingPageComponent},
  {path: 'forget-password', component: ForgetPasswordComponent},
  {path: 'reset-password', component: ResetPasswordComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
  // providers:[
  //   LoginServiceService,
  //   {
  //     provide: HTTP_INTERCEPTORS,
  //     useClass: tokenInterceptorInterceptor,
  //     multi: true
  //   }
  // ]
})
export class AppRoutingModule { }
