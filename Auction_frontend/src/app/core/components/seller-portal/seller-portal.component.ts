import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ItemListingServiceService } from '../../services/item-listing-service/item-listing-service.service';
import { ActivatedRoute } from '@angular/router';
import { Router } from '@angular/router';

@Component({
  selector: 'app-seller-portal',
  templateUrl: './seller-portal.component.html',
  styleUrl: './seller-portal.component.css'
})
export class SellerPortalComponent implements OnInit  {

  items: any = [];

  constructor(
    private fb: FormBuilder,
    private itemService: ItemListingServiceService,
    private route: ActivatedRoute,
    private router: Router
  ){

  }

  ngOnInit(): void {
    this.itemService.getAllItems().subscribe(
      (response) => {
        this.items = response;
        console.log(JSON.stringify(this.items, null, 2));
      },
      (error) => {
        console.error('Error fetching items:', error);
      }
    );
  }
}
