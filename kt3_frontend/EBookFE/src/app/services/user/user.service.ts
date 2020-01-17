import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private url: String;

  constructor(private httpClient: HttpClient) {
    this.url = "http://localhost:8080/userDb";
  }

  getAllByRole(role: String){
    return this.httpClient.get(`${this.url}/get/${role}/all`) as Observable<any>
  }
}
