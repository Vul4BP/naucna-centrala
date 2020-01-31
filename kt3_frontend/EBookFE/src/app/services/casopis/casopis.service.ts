import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CasopisService {

  private url: String;

  constructor(private httpClient: HttpClient) {
    this.url = "https://localhost:5005/casopis";
  }

  getByTaskId(taskId: String){
    return this.httpClient.get(`${this.url}/get/${taskId}`) as Observable<any>
  }

  getAll(){
    return this.httpClient.get(`${this.url}/get/all`) as Observable<any>
  }

  kupiCasopis(id: Number){
    return this.httpClient.get(`${this.url}/kupi/${id}`) as Observable<any>
  }

  subscribeCasopis(id: Number){
    return this.httpClient.get(`${this.url}/subscribe/${id}`) as Observable<any>
  }
}
