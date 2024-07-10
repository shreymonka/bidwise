import { Component } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-pricing',
  templateUrl: './pricing.component.html',
  styleUrls: ['./pricing.component.css']
})
export class PricingComponent {
  pricingForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
    private router: Router
  ) {
    this.pricingForm = this.fb.group({
      cardNumber: [''],
      expiryDate: [''],
      cvv: [''],
      cardHolderName: [''],
      email: ['']
    });
  }

  submitPayment(): void {
    const paymentDetails = this.pricingForm.value;
    const token = localStorage.getItem('token');  // Assuming you store JWT in localStorage after login
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    this.http.post('http://172.17.3.242/api/v1/user/upgrade-to-premium', paymentDetails, { headers }).subscribe(
      (response: any) => {
        Swal.fire({
          title: 'Success!',
          text: 'You have been upgraded to a premium member!',
          icon: 'success',
          confirmButtonText: 'OK'
        }).then(() => {
          this.router.navigate(['/postLogin']);
        });
      },
      (error) => {
        console.error('Payment submission error:', error);
        Swal.fire({
          title: 'Error!',
          text: 'There was an error processing your payment. Please try again.',
          icon: 'error',
          confirmButtonText: 'OK'
        });
      }
    );
  }
}
