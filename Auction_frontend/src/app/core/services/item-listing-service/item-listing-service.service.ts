import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs';
import { Route, Router } from '@angular/router';
@Injectable({
  providedIn: 'root'
})
export class ItemListingServiceService {
  addItemEndPoint: string = 'http://localhost:8080/api/v1/item/additem';
  getAllItemsEndPoint: string = 'http://localhost:8080/api/v1/item/getitems';
  deleteItemEndPoint: String = 'http://localhost:8080/api/v1/item/deleteItemListed';

  constructor(private http: HttpClient,public router:Router) { }
  addItemForAuction(itemDetails:any): Observable<any>{
    let api = `${this.addItemEndPoint}`;
    console.log("Test payload"+api);
    return this.http.post(api,itemDetails).pipe(catchError(this.handleError));
  }


  getAllItems():Observable<any>{
    let api = `${this.getAllItemsEndPoint}`;
    return this.http.get(api).pipe(catchError(this.handleError));
  }

  deleteItem(itemId: number): Observable<any> {
    return this.http.delete(`${this.deleteItemEndPoint}`, { params: { itemId: itemId.toString() } }).pipe(catchError(this.handleError));
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
