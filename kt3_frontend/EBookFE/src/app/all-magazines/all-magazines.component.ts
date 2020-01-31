import { Component, OnInit } from '@angular/core';
import { CasopisService } from '../services/casopis/casopis.service';

@Component({
  selector: 'app-all-magazines',
  templateUrl: './all-magazines.component.html',
  styleUrls: ['./all-magazines.component.css']
})
export class AllMagazinesComponent implements OnInit {

  private allMagazines = [];

  constructor(private casopisService : CasopisService) { }

  ngOnInit() {
    this.casopisService.getAll()
      .subscribe(
        data => {
          console.log(data);
          this.allMagazines = data;
        },
        error => {
          console.log("Error");
        })
  }

  kupiCasopis(id : Number){
    //console.log(id);
    this.casopisService.kupiCasopis(id)
    .subscribe(
      data => {
        console.log(data);
        window.location.replace(data['redirectUrl']);
      },
      error => {
        console.log("Error");
      })
  }

  checkIfPaypal(id : Number) : boolean{
    let res = false;
    let magazine = this.allMagazines.find(x => x.id == id);
    if(magazine != null && magazine.naciniPlacanja.find(x => x.name == "Paypal") != null){
      res = true;
    }
    return res;
  }

  subscribeCasopis(id : Number){
    //console.log(id);
    this.casopisService.subscribeCasopis(id)
    .subscribe(
      data => {
        console.log(data);
        window.location.replace(data['redirectUrl']);
      },
      error => {
        console.log("Error");
      })
  }

}
