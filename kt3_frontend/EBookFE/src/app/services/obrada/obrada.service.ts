import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class ObradaService {

  private url: String;

  constructor(private httpClient: HttpClient) {
    this.url = "https://localhost:5005/obradateksta";
  }

  startProcess(username: String){
    return this.httpClient.get(`${this.url}/start/${username}`) as Observable<any>
  }

  izaberiMagazin(user: any, taskId: String) {
    return this.httpClient.post(`${this.url}/post/pickmagazine/${taskId}`, user) as Observable<any>;
  }

  statusPlacanja(status : String, username : String){
    return this.httpClient.post(`${this.url}/post/status/${username}`, status) as Observable<any>;
  }

  getRadForm(taskId: string){
    return this.httpClient.get(`${this.url}/get/podacioraduform/${taskId}`) as Observable<any>
  }

  dodajRad(user: any, taskId: String) {
    return this.httpClient.post(`${this.url}/post/podacioraduform/${taskId}`, user) as Observable<any>;
  }

  postFile(data: any, fileToUpload: File, taskId : String): Observable<boolean> {
    const endpoint = `${this.url}/post/podacioraduform/${taskId}`;
    const formData: FormData = new FormData();
    formData.append('file', fileToUpload, fileToUpload.name);
    formData.append('data', JSON.stringify(data));
    return this.httpClient.post(endpoint, formData) as Observable<any>;
  }

  getActiveTasksByName(username: string, taskName: string){
    return this.httpClient.get(`${this.url}/get/tasks/active/${username}/${taskName}`) as Observable<any>
  }

  getMultipleActiveTasksByName(username: string, taskName: string){
    return this.httpClient.get(`${this.url}/get/tasks/multiple/active/${username}/${taskName}`) as Observable<any>
  }

  getPregledanjeRadovaForm(taskId: string){
    return this.httpClient.get(`${this.url}/get/pregledanjeRadovaForm/${taskId}`) as Observable<any>
  }

  postPregledanjeRadovaForm(data: any, taskId: String) {
    return this.httpClient.post(`${this.url}/post/pregledanjeRadovaForm/${taskId}`, data) as Observable<any>;
  }

  getPregledanjePdfaForm(taskId: string){
    return this.httpClient.get(`${this.url}/get/pregledanjePdfaForm/${taskId}`) as Observable<any>
  }

  getFile(processInstance: string){
    return this.httpClient.get(`${this.url}/get/file/${processInstance}`, {observe: 'response', responseType: 'blob'}) as Observable<any>
  }

  postPregledanjePdfaForm(data: any, taskId: String) {
    return this.httpClient.post(`${this.url}/post/pregledanjePdfaForm/${taskId}`, data) as Observable<any>;
  }

  getKorekcijaRadovaForm(taskId: string){
    return this.httpClient.get(`${this.url}/get/korekcijaRadovaForm/${taskId}`) as Observable<any>
  }

  postFileChange(data: any, fileToUpload: File, taskId : String): Observable<boolean> {
    const endpoint = `${this.url}/post/korekcijaRadovaForm/${taskId}`;
    const formData: FormData = new FormData();
    formData.append('file', fileToUpload, fileToUpload.name);
    formData.append('data', JSON.stringify(data));
    return this.httpClient.post(endpoint, formData) as Observable<any>;
  }

  getIzborRecenzenataForm(taskId: string){
    return this.httpClient.get(`${this.url}/get/izborRecenzenataForm/${taskId}`) as Observable<any>
  }

  postIzborRecenzenataForm(data: any, taskId: String) {
    return this.httpClient.post(`${this.url}/post/izborRecenzenataForm/${taskId}`, data) as Observable<any>;
  }

  getRecenzijaRadovaForm(taskId: string){
    return this.httpClient.get(`${this.url}/get/recenzijaRadovaForm/${taskId}`) as Observable<any>
  }

  postRecenzijaRadovaForm(data: any, taskId: String) {
    return this.httpClient.post(`${this.url}/post/recenzijaRadovaForm/${taskId}`, data) as Observable<any>;
  }

  getIzborNovogRecenzentaForm(taskId: string){
    return this.httpClient.get(`${this.url}/get/izborNovogRecenzenta/${taskId}`) as Observable<any>
  }

  postIzborNovogRecenzentaForm(data: any, taskId: String) {
    return this.httpClient.post(`${this.url}/post/izborNovogRecenzenta/${taskId}`, data) as Observable<any>;
  }

  getCitanjeKomentaraForm(taskId: string){
    return this.httpClient.get(`${this.url}/get/citanjeKomentaraForm/${taskId}`) as Observable<any>
  }

  postCitanjeKomentaraForm(data: any, taskId: String) {
    return this.httpClient.post(`${this.url}/post/citanjeKomentaraForm/${taskId}`, data) as Observable<any>;
  }

  getIspravkaRadovaForm(taskId: string){
    return this.httpClient.get(`${this.url}/get/ispravkaRadovaForm/${taskId}`) as Observable<any>
  }

  getPregledIzmenaForm(taskId: string){
    return this.httpClient.get(`${this.url}/get/pregledIzmenaForm/${taskId}`) as Observable<any>
  }

  postPregledIzmenaForm(data: any, taskId: String) {
    return this.httpClient.post(`${this.url}/post/pregledIzmenaForm/${taskId}`, data) as Observable<any>;
  }
}
