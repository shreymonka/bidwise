import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-signup-page',
  templateUrl: './signup-page.component.html',
  styleUrl: './signup-page.component.css'
})
export class SignupPageComponent {
  constructor(private router: Router) { }
  LoginRedirect(){
    this.router.navigate(['/login']);
  }

  postLoginRedirect(){
    this.router.navigate(['/postLogin']);
  }
}
