import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { LoginServiceService } from '../../services/login-service/login-service.service';
import Swal from 'sweetalert2';
@Component({
  selector: 'app-login-page',
  templateUrl: './login-page.component.html',
  styleUrls: ['./login-page.component.css']
})
export class LoginPageComponent {
  email: string = '';
  password: string = '';

  constructor(private router: Router, private http: HttpClient, private loginService: LoginServiceService) { }

  SignupRedirect(){
    this.router.navigate(['/signup']);
  }

  onSubmit(){
    const loginData = { email: this.email, password: this.password };
    this.loginService.login(loginData).subscribe(
      (response: any) => {
        if (response.access_token) {
          this.loginService.storeToken(response.access_token);
          Swal.fire({
            title: 'Login Successful',
            text: 'You have successfully logged in!',
            icon: 'success',
            confirmButtonText: 'Ok'
          }).then(() => {
            this.router.navigate(['/postLogin']); 
          });
        }
      },
      (error) => {
        console.error('Login failed', error);
        // Handle login failure (show error message, etc.)
      }
    );
  }
}
