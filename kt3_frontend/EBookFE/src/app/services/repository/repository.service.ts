import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RepositoryService {

  private url: String;

  constructor(private httpClient: HttpClient) {
    this.url = "https://localhost:5005/process";
  }

  claimTask(taskId){
    return this.httpClient.post(`${this.url}/tasks/claim/${taskId}`, null) as Observable<any>
  }

  completeTask(taskId){
    return this.httpClient.post(`${this.url}/tasks/complete/${taskId}`, null) as Observable<any>
  }

  getTasks(processInstance : string){
    return this.httpClient.get(`${this.url}/tasks/get/${processInstance}`) as Observable<any>
  }
}
