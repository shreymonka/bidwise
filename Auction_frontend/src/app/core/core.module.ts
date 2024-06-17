import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LoginPageComponent } from './components/login-page/login-page.component';
import { SignupPageComponent } from './components/signup-page/signup-page.component';
import { LandingPageComponent } from './components/landing-page/landing-page.component';
import { SharedModule } from '../shared/shared.module';
import { AboutUsComponent } from './components/about-us/about-us.component';
import { RouterModule } from '@angular/router';
import { PostLoginLandingPageComponent } from './components/post-login-landing-page/post-login-landing-page.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

@NgModule({
  declarations: [
    LoginPageComponent, 
    SignupPageComponent,
    LandingPageComponent,
    AboutUsComponent,
    PostLoginLandingPageComponent],
  exports : [
    LandingPageComponent,
    AboutUsComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    RouterModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule
  ]
})
export class CoreModule { }
