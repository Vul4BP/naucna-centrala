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

}
