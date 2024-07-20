import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserProfileService {

  private endPoint = 'http://localhost:8080/api/v1/user/profile';

  constructor(private http: HttpClient) { }

  // Method to fetch user profile data
  getUserProfile(): Observable<any> {
    return this.http.get<any>(`${this.endPoint}`);
  }

  // Method to fetch chart data
  getChartData(): Observable<any> {
    return this.http.get<any>(`${this.endPoint}`);
  }
}
