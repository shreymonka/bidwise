import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Client, Frame, Stomp, StompSubscription } from '@stomp/stompjs';
import { ToastrService } from 'ngx-toastr';

@Injectable({
  providedIn: 'root',
})
export class AuctionServiceService {
  private baseUrl = 'ws://172.17.3.242:8080/gs-guide-websocket';
  constructor(private http: HttpClient, private toastr: ToastrService) {}
  private client: any;

  connect(formdata: FormGroup) {
    console.log("The base URL is:"+this.baseUrl)
    var socket = new WebSocket(this.baseUrl, 'v10.stomp');
    this.client = Stomp.over(socket);
    this.client.connect({}, (x: Frame) => {
      console.log('Connected!!!!!!!!: ' + x);
      this.client.subscribe(
        '/topic/greetings/' + formdata.value.itemId,
        (y: any) => {
          console.log(y);
          this.toastr.success(
            'The current Highest Bid is:' + y.body,
            'Message',
            { timeOut: 10000 }
          );
          formdata.patchValue({
            currentBid: y.body,
          });
        }
      );
    });
  }

  publish(formdata: FormGroup,accessToken: string |null) {
    console.log('The base URL is:'+this.baseUrl)
    const headers = {
      Authorization: accessToken,
      // Add other headers as needed
    };
    var socket = new WebSocket(this.baseUrl, 'v10.stomp');
    this.client = Stomp.over(socket);
    this.client.connect({}, (x: any) => {
      console.log(formdata.value.itemId);
      console.log('Connected !!!');
      this.client.publish({
        destination: '/app/bid/' + formdata.value.itemId,
        body: formdata.value.bid,
        headers:headers
      });
    });
  }

  getBidResponse(formdata: FormGroup) {
    this.connect(formdata);
  }


  getAuctionDetails(){
    //Need to fetch the auction details
  }
}
