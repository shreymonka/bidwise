import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import {
  HttpClient,
  HttpHeaders,
  HttpErrorResponse,
} from '@angular/common/http';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})

export class SignUpServiceService {
  endpoint: string = 'http://172.17.3.242:8080/api/v1/user/register';
  locationEndpoint : string = 'http://172.17.3.242:8080/api/v1/location/country';
  cityEndpoint : String = 'http://172.17.3.242:8080/api/v1/location/cities/';
  // headers = new HttpHeaders().set('Content-Type', 'application/json');
  constructor(private http: HttpClient, public router: Router) { }

  signUpUser(userDetails:any): Observable<any>{
    let api = `${this.endpoint}`;
    console.log("Test payload"+api);
    return this.http.post(api,userDetails).pipe(catchError(this.handleError));
  }

  fetchAllCountries(): Observable<any>{
    let api = `${this.locationEndpoint}`;
    return this.http.get(api).pipe(catchError(this.handleError))
  }

  fetchAllCities(countryId: String){
    return this.http.get<any>(`${this.cityEndpoint}`+countryId).pipe(catchError(this.handleError))
  }

  handleError(error: HttpErrorResponse) {
    let msg = '';
    if (error.error instanceof ErrorEvent) {
      // client-side error
      msg = error.error.message;
    } else {
      // server-side error
      msg = `Error Code: ${error.status}\nMessage: ${error.message}`;
    }
    return throwError(msg);
  }
}
