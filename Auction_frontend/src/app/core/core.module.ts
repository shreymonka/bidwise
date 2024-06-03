import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LoginPageComponent } from './components/login-page/login-page.component';
import { SignupPageComponent } from './components/signup-page/signup-page.component';
import { LandingPageComponent } from './components/landing-page/landing-page.component';
import { SharedModule } from '../shared/shared.module';

@NgModule({
  declarations: [
    LoginPageComponent, 
    SignupPageComponent,
    LandingPageComponent],
  exports : [],
  imports: [
    CommonModule,
    SharedModule,
  ]
})
export class CoreModule { }
