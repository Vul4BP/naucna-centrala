import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private url: String;

  constructor(private httpClient: HttpClient) {
    this.url = "http://localhost:8080";
   }

  login(jwtAuthenticationRequest: any) {
    return this.httpClient.post<any>(`${this.url}/auth/login`, jwtAuthenticationRequest) as Observable<any>;
  }

  logout() {
    //treba odraditi i sa serverske strane
    localStorage.removeItem('token');
    localStorage.removeItem('roles');
    return true;
  } 

  isLoggedIn(){
    if(localStorage.getItem("token")!=null){
      return true;
    }else{
      return false;
    }
  }

  isAdmin(){
    if(localStorage.getItem("token") != null){
      let roles = JSON.parse(localStorage.getItem("roles"));
      let retVal = false;
      roles.forEach(role => {   
        if(role['authority'] == "ROLE_ADMIN"){
          retVal = true;
        }
      });

      return retVal;
    }else{
      return false;
    }
  }

  isUrednik(){
    if(localStorage.getItem("token") != null){
      let roles = JSON.parse(localStorage.getItem("roles"));
      let retVal = false;
      roles.forEach(role => {   
        if(role['authority'] == "ROLE_UREDNIK"){
          retVal = true;
        }
      });

      return retVal;
    }else{
      return false;
    }
  }

  getToken(): any{
    return localStorage.getItem('token');
  }

  parseToken(token:any): any {
    let jwtData = token.split('.')[1];
    let decodedJwtJsonData = window.atob(jwtData);
    let decodedJwtData = JSON.parse(decodedJwtJsonData);
    //console.log(decodedJwtData);
    return decodedJwtData;
  }

  getUsername(): any{
    let tkn = localStorage.getItem("token");
    if (tkn == null) {
      return "";
    }

    let jwtParsed = this.parseToken(tkn);
    return jwtParsed.sub;
  }

}
