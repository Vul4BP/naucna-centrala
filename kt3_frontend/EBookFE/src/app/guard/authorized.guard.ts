import { Injectable } from '@angular/core';
import { CanActivate } from '@angular/router';

@Injectable()
export class Authorized implements CanActivate {

  constructor() {}

  canActivate() {
    if(localStorage.getItem("token")!=null){
      return true;
    }else{
      return false;
    }
  }
}