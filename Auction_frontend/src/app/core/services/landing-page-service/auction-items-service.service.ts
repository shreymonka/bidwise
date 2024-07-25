import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuctionItemsServiceService {
  private apiUrl = 'http://172.17.3.242:8080/api/v1';
  constructor(private http: HttpClient,public router:Router) { }

  getUpcomingAuctions(): Observable<any> {
    return this.http.get<any[]>(`${this.apiUrl}/auction/upcoming`);
  }
}


