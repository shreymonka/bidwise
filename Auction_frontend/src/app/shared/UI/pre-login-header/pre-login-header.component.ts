import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-pre-login-header',
  templateUrl: './pre-login-header.component.html',
  styleUrl: './pre-login-header.component.css'
})
export class PreLoginHeaderComponent {
  constructor(private router: Router) { }

  LoginRedirect(){
    this.router.navigate(['/login']);
  }

  SignupRedirect(){
    this.router.navigate(['/signup']);
  }

}
