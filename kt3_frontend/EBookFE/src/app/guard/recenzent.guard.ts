import { Injectable } from '@angular/core';
import { CanActivate } from '@angular/router';

@Injectable()
export class Recenzent implements CanActivate {

  constructor() {}

  canActivate() {
    if(localStorage.getItem("token") != null){
      let retVal = false;
      let roles = JSON.parse(localStorage.getItem("roles"));
      roles.forEach(role => { 
        if(role['authority'] == "ROLE_RECENZENT"){
          retVal = true;
        }
      });

      return retVal;
    }else{
      return false;
    }
  }
}