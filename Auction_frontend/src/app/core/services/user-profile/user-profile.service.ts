import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserProfileService {

  // private endPoint = 'http://localhost:8080/api/v1/profile';
  private endPoint = 'http://172.17.3.242:8080/api/v1/profile';
  constructor(private http: HttpClient) { }

  getUserProfile(userId: number): Observable<any> {
    return this.http.get<any>(`${this.endPoint}/details?userId=${userId}`);
  }

  getAuctionParticipation(userId: number): Observable<any> {
    return this.http.get<any>(`${this.endPoint}/auctionsParticipated?userId=${userId}`);
  }

  getBidStats(userId: number): Observable<any> {
    return this.http.get<any>(`${this.endPoint}/bidStats?userId=${userId}`);
  }

  getCategoryBidStats(userId: number): Observable<any> {
    return this.http.get<any>(`${this.endPoint}/categoryBidStats?userId=${userId}`);
  }
}
