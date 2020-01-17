import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class NaucnaOblastService {

  private url: String;

  constructor(private httpClient: HttpClient) {
    this.url = "http://localhost:8080/naucnaoblast";
  }

  getAll(){
    return this.httpClient.get(`${this.url}/get/all`) as Observable<any>
  }
}
