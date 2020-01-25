import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class NacinPlacanjaService {

  private url: String;

  constructor(private httpClient: HttpClient) {
    this.url = "https://localhost:5005/nacinplacanja";
  }

  getAll(){
    return this.httpClient.get(`${this.url}/get/all`) as Observable<any>
  }
}
