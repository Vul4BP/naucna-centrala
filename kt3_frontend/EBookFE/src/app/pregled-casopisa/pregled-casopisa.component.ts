import { Component, OnInit } from '@angular/core';
import { DodavanjeCasopisaService } from '../services/dodavanjecasopisa/dodavanje-casopisa.service';
import { AuthService } from '../services/auth/auth-service.service';
import { Router } from '@angular/router';
import { CasopisService } from '../services/casopis/casopis.service';

@Component({
  selector: 'app-pregled-casopisa',
  templateUrl: './pregled-casopisa.component.html',
  styleUrls: ['./pregled-casopisa.component.css']
})
export class PregledCasopisaComponent implements OnInit {

  private tasks = [];
  private username = "";
  private formFieldsDto = null;
  private formFields = [];
  public processInstance = "";
  private enumValues = [];
  private taskId = "";
  private casopis = null;
 
  constructor(private casopisService : CasopisService, private authService : AuthService,
     private dodavanjeCasopisaService : DodavanjeCasopisaService, private router: Router) { }

  ngOnInit() {
    this.username = this.authService.getUsername();
    this.getTasks();
  }

  getTasks(){
    let x = this.dodavanjeCasopisaService.getActiveTasks(this.username);

    x.subscribe(
      res => {
        //console.log(res);
        this.tasks = res;
      },
      err => {
        console.log("Error occured");
      }
    );
  }

  getPregledCasopisaForm(taskId : string){
    this.casopis = null;
    this.taskId = taskId;
    console.log(taskId);

    let x = this.dodavanjeCasopisaService.getPregledCasopisaForm(taskId);

    x.subscribe(
      res => {
        console.log(res);
        this.formFieldsDto = res;
        this.formFields = res.formFields;
        this.processInstance = res.processInstanceId;
        this.formFields.forEach( (field) =>{  
          if(field.type.name=='enum'){
            this.enumValues = Object.keys(field.type.values);
          }
        });

        let y = this.casopisService.getByTaskId(taskId);

        y.subscribe(
          res => {
            console.log(res);
            this.casopis = res;
          },
          err => {
            console.log("Error occured");
          }
        );
      },
      err => {
        console.log("Error occured");
      }
    );
   }

   onSubmit(value, form){
    let o = new Array();
    for (var property in value) {
      //console.log(property);
      //console.log(value[property]);
      o.push({fieldId : property, fieldValue : value[property]});
    }  
    
    console.log(o);
    
    let x = this.dodavanjeCasopisaService.dodajCasopisStatus(o, this.formFieldsDto.taskId);

    x.subscribe(
      res => {
        this.tasks = [];
        this.getTasks();
        this.formFields = [];
        this.casopis = null;
        //this.router.navigate(['/regresult', this.processInstance]);
      },
      err => {
        console.log("Error occured");
      }
    );
  }

}
