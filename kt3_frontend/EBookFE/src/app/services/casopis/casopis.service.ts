import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CasopisService {

  private url: String;

  constructor(private httpClient: HttpClient) {
    this.url = "http://localhost:8080/casopis";
  }

  getByTaskId(taskId: String){
    return this.httpClient.get(`${this.url}/get/${taskId}`) as Observable<any>
  }
}
