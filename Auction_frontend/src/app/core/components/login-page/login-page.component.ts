import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login-page',
  templateUrl: './login-page.component.html',
  styleUrl: './login-page.component.css'
})
export class LoginPageComponent {
  constructor(private router: Router) { }

  SignupRedirect(){
    this.router.navigate(['/signup']);
  }

  postLoginRedirect(){
    this.router.navigate(['/postLogin']);
  }
}
