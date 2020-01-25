import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RegisterService {

  private url: String;

  constructor(private httpClient: HttpClient) {
    this.url = "https://localhost:5005/registration";
  }

  startProcess(){
    return this.httpClient.get(`${this.url}/start`) as Observable<any>
  }

  getRegStatus(processInstance : string){
    return this.httpClient.get(`${this.url}/get/status/${processInstance}`) as Observable<any>
  }

  getRecenzentProposals(username: string){
    return this.httpClient.get(`${this.url}/get/tasks/active/${username}`) as Observable<any>
  }

  getRecenzentiForm(taskId: string){
    return this.httpClient.get(`${this.url}/get/recenzentiform/${taskId}`) as Observable<any>
  }

  registerUser(user: any, taskId: String) {
    return this.httpClient.post(`${this.url}/post/registerform/${taskId}`, user) as Observable<any>;
  }

  recenzentStatusUser(data: any, taskId: String) {
    return this.httpClient.post(`${this.url}/post/recenzentiform/${taskId}`, data) as Observable<any>;
  }
}
