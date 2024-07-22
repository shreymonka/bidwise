import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import {
  HttpClient,
  HttpHeaders,
  HttpErrorResponse,
} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class AddFundsService {
  // endpoint: string = 'http://localhost:8080/api/v1/account/addFunds';
  endpoint: string = 'http://172.17.3.242:8080/api/v1/account/addFunds';

  constructor(private http: HttpClient) {}

  addFunds(userId: number, amount: number): Observable<any> {
    const url = `${this.endpoint}`;
    const headers = new HttpHeaders({
      'Content-Type': 'application/x-www-form-urlencoded'
    });

    const body = new URLSearchParams();
    body.set('userId', userId.toString());
    body.set('amount', amount.toString());

    return this.http.post(url, body.toString(), { headers }).pipe(
      catchError(this.handleError)
    );
  }

  private handleError(error: HttpErrorResponse) {
    let errorMessage = 'An unknown error occurred!';
    if (error.error instanceof ErrorEvent) {
      // Client-side error
      errorMessage = `Client-side error: ${error.error.message}`;
    } else {
      // Server-side error
      errorMessage = `Server-side error: ${error.status} ${error.message}`;
    }
    return throwError(errorMessage);
  }
}