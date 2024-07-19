import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PostLoginLandingPageServiceService {
  private apiUrl = 'http://172.17.3.242:8080/api/v1';
  constructor(private http: HttpClient,public router:Router) { }
  
  getUpcomingAuctions(): Observable<any> {
    return this.http.get<any[]>(`${this.apiUrl}/auction/items/existing-user`);
  }

}
