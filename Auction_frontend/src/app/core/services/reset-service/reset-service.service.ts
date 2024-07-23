import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
@Injectable({
  providedIn: 'root'
})
export class ResetServiceService {

  private apiUrl = 'http://172.17.3.242:8080/api/v1';

  constructor(private http: HttpClient) { }

  sendResetLink(email: string): Observable<any> {
    console.log(email);
    return this.http.post(`${this.apiUrl}/user/forgot-password`,{email});
  }

  resetPassword(token: string, newPassword: string): Observable<any> {
    console.log(token + newPassword);
    return this.http.post(`${this.apiUrl}/user/reset-password`, {token, newPassword});
  }}
