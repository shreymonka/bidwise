import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, Observable, throwError } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TradebookService {
  getAllTradesEndpoint: string = 'http://localhost:8080/api/v1/user/getTradebook';

  constructor(private http: HttpClient,public router:Router) { }

  getAllTrades():Observable<any>{
    let api = `${this.getAllTradesEndpoint}`;
    return this.http.get(api).pipe(catchError(this.handleError));
  }
  private handleError(error: any): Observable<never> {
    let errorMessage = '';
    if (error.error instanceof ErrorEvent) {
      // Client-side error
      errorMessage = `Error: ${error.error.message}`;
    } else {
      // Server-side error
      errorMessage = `Error Code: ${error.status}\nMessage: ${error.message}`;
    }
    return throwError(errorMessage);
  }

}
