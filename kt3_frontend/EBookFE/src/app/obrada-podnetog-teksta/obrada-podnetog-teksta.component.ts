import { Component, OnInit } from '@angular/core';
import { CasopisService } from '../services/casopis/casopis.service';
import { ObradaService } from '../services/obrada/obrada.service';
import { AuthService } from '../services/auth/auth-service.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-obrada-podnetog-teksta',
  templateUrl: './obrada-podnetog-teksta.component.html',
  styleUrls: ['./obrada-podnetog-teksta.component.css']
})
export class ObradaPodnetogTekstaComponent implements OnInit {

  private formFieldsDto = null;
  private formFields = [];
  public processInstance = "";
  //private enumValues = {};
  private tasks = [];
  private listaCasopisa = [];
  private message = "";
  private username = "";

  constructor(private casopisService : CasopisService, private obradaService : ObradaService, private authService : AuthService, private router : Router) {
    
    this.username = this.authService.getUsername();

    let y = casopisService.getAll();
    y.subscribe(
      res => {
        console.log(res);
        this.listaCasopisa = res;

        let x = obradaService.startProcess(this.username);
        x.subscribe(
          res => {
            console.log(res);
            this.formFieldsDto = res;
            this.formFields = res.formFields;
            this.processInstance = res.processInstanceId;
          },
          err => {
            console.log("Error occured");
            this.message = "Desila se greska.";
          }
        );
      },
      error => {
        console.log("Some error");
      }
    );
   }

  ngOnInit() {
  }

  onSubmit(value, form){
    let o = new Array();
    for (var property in value) {
        o.push({fieldId : property, fieldValue : value[property]});
    }
    console.log(o);

    let x = this.obradaService.izaberiMagazin(o, this.formFieldsDto.taskId);
    x.subscribe(
      res => {
        console.log(res);
        if(res['tip_placanja'] == "autorima" && res['aktivna_clanarina'] == "false"){
          this.router.navigate(['/uplataclanarine', this.processInstance]);
        }else{
          this.router.navigate(['/podacioradu', this.processInstance]);
        }
      },
      err => {
        console.log("Error occured");
        this.message = "Validacija nije uspesna.";
        //this.getTaskId();
      }
    );
  }

}
