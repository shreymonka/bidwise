import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { AuctionServiceService } from '../../services/auction-service/auction-service.service';
import { ToastrService } from 'ngx-toastr';
import { LoginServiceService } from '../../services/login-service/login-service.service';
@Component({
  selector: 'app-auction',
  templateUrl: './auction.component.html',
  styleUrl: './auction.component.css',
})
export class AuctionComponent implements OnInit {
  formdata: FormGroup;
  userDetails: any;
  constructor(
    private auctionService: AuctionServiceService,
    private loginService:LoginServiceService
  ) {
    this.formdata = new FormGroup({
      currentBid: new FormControl(''),
      itemId: new FormControl(''),
      bid: new FormControl(''),
    });
  }

  ngOnInit(): void {}

  onSubmit() {
    console.log('Typed');
    this.auctionService.getBidResponse(this.formdata);
  }

  onSubmit2(){
    console.log("Typed OnSubmit2")
    this.auctionService.publish(this.formdata,this.loginService.getToken());
  }
}
