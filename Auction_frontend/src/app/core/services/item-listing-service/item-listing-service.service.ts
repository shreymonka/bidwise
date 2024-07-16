import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs';
import { Route, Router } from '@angular/router';
@Injectable({
  providedIn: 'root'
})
export class ItemListingServiceService {
  endpoint: string = 'http://localhost:8080/api/v1/item/additem';

  constructor(private http: HttpClient,public router:Router) { }
  addItemForAuction(itemDetails:any): Observable<any>{
    let api = `${this.endpoint}`;
    console.log("Test payload"+api);
    return this.http.post(api,itemDetails).pipe(catchError(this.handleError));
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
