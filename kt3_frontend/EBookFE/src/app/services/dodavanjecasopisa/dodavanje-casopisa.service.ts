import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DodavanjeCasopisaService {

  private url: String;

  constructor(private httpClient: HttpClient) {
    this.url = "https://localhost:5005/dodavanjecasopisa";
  }

  startProcess(username: String){
    return this.httpClient.get(`${this.url}/start/${username}`) as Observable<any>
  }

  addCasopis(user: any, taskId: String) {
    return this.httpClient.post(`${this.url}/post/casopisform/${taskId}`, user) as Observable<any>;
  }

  getActiveTasks(username: string){
    return this.httpClient.get(`${this.url}/get/tasks/active/${username}`) as Observable<any>
  }

  getActiveTasksByName(username: string, taskName: string){
    return this.httpClient.get(`${this.url}/get/tasks/active/${username}/${taskName}`) as Observable<any>
  }

  getDodavanjeUrednikaIRecenzenataForm(taskId: string){
    return this.httpClient.get(`${this.url}/get/dodavanjeUrednikaIRecenzenataform/${taskId}`) as Observable<any>
  }

  dodajUrednikeIRecenzente(data: any, taskId: String) {
    return this.httpClient.post(`${this.url}/post/dodavanjeUrednikaIRecenzenataform/${taskId}`, data) as Observable<any>;
  }

  getPregledCasopisaForm(taskId: string){
    return this.httpClient.get(`${this.url}/get/pregledcasopisaform/${taskId}`) as Observable<any>
  }

  dodajCasopisStatus(data: any, taskId: String) {
    return this.httpClient.post(`${this.url}/post/pregledcasopisaform/${taskId}`, data) as Observable<any>;
  }

  getDodavanjeCasopisaForm(taskId: string){
    return this.httpClient.get(`${this.url}/get/dodavanjecasopisa/${taskId}`) as Observable<any>
  }

  getPotvrdaMikroservisa(taskId: string){
    return this.httpClient.get(`${this.url}/get/potvrdamikroservisa/${taskId}`) as Observable<any>
  }

  proveraPotvrdjenihMikroservisa(data: any, taskId: String) {
    return this.httpClient.post(`${this.url}/post/potvrdamikroservisa/${taskId}`, data) as Observable<any>;
  }
}
