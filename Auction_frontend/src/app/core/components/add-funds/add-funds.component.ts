import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AddFundsService } from '../../services/add-funds/add-funds.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-add-funds',
  templateUrl: './add-funds.component.html',
  styleUrl: './add-funds.component.css'
})
export class AddFundsComponent {

  addFundsForm: FormGroup;
  successMessage: string = '';
  errorMessage: string = '';

  constructor(
    private fb: FormBuilder,
    private addFundService: AddFundsService
  ){ 
    this.addFundsForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      cardNumber: ['', Validators.required],
      expiry: ['', Validators.required],
      cvc: ['', Validators.required],
      cardholderName: ['', Validators.required],
      address: ['', Validators.required],
      country: ['', Validators.required],
      city: ['', Validators.required],
      amount: ['', [Validators.required, Validators.pattern('^[0-9]*$')]],
      currency: ['', Validators.required],
      terms: [false, Validators.requiredTrue]
    });
  }
  
  onSubmit(): void {
    if (this.addFundsForm.valid) {
      const userId = 1; // Replace with the actual userId from your auth logic or a hidden field in the form
      const amount = this.addFundsForm.get('amount')?.value;

      this.addFundService.addFunds(userId, amount).subscribe(
        response => {
          this.successMessage = 'Funds added successfully';
          this.errorMessage = '';
          this.addFundsForm.reset();
        },
        error => {
          this.errorMessage = 'Error adding funds: ' + error;
          this.successMessage = '';
        }
      );
    }
  }

}
