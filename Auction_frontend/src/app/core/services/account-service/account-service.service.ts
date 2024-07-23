import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AccountServiceService {
  private baseUrl = "http://172.17.3.242:8080/api/v1";
  constructor(private http: HttpClient) { }

  getAccountFunds(){
    return this.http.get(this.baseUrl+'/account/balance');
  }
}
