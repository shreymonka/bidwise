import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';
import { TradebookService } from '../../services/tradebook-service/tradebook.service';

@Component({
  selector: 'app-tradebook',
  templateUrl: './tradebook.component.html',
  styleUrl: './tradebook.component.css'
})
export class TradebookComponent {
  trades: any = [];

  constructor(
    private fb: FormBuilder,
    private tradebookservice: TradebookService,
    private route: ActivatedRoute,
    private router: Router
  ){

  }
  ngOnInit(): void {
    this.tradebookservice.getAllTrades().subscribe(
      (response) => {
        this.trades = response;
        console.log(this.trades);
        
      },
      (error) => {
        console.error('Error fetching items:', error);
      }
    );
  }
}
