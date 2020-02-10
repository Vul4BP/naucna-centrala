import { Component, OnInit } from '@angular/core';
import { AuthService } from '../services/auth/auth-service.service';
import { ObradaService } from '../services/obrada/obrada.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-pregled-rada',
  templateUrl: './pregled-rada.component.html',
  styleUrls: ['./pregled-rada.component.css']
})
export class PregledRadaComponent implements OnInit {

  private tasks = [];
  private username = "";
  private taskId = "";
  private formFieldsDto = null;
  private formFields = [];
  public processInstance = "";
  private enumValues = [];
  private message = "";

  private relevantan = true;

  constructor(private authService : AuthService, private obradaService : ObradaService, private router : Router) { }

  ngOnInit() {
    this.username = this.authService.getUsername();
    this.getTasks();
  }

  getTasks(){
    let x = this.obradaService.getActiveTasksByName(this.username, "Provera rada");

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

  getPregledanjeRadovaForm(taskId : string){
    this.taskId = taskId;
    console.log(taskId);

    let x = this.obradaService.getPregledanjeRadovaForm(taskId);

    x.subscribe(
      res => {
        console.log(res);
        this.formFieldsDto = res;
        this.formFields = res.formFields;
        this.processInstance = res.processInstanceId;
        this.formFields.forEach( (field) =>{
          if( field.type.name=='enum'){
            this.enumValues = Object.keys(field.type.values);
          }
        });
      },
      err => {
        console.log("Error occured");
      }
    );
  }

  onSubmit(value, form){
    let o = new Array();
    for (var property in value) {
      o.push({fieldId : property, fieldValue : value[property]});
    }  
    console.log(o);
    
    let x = this.obradaService.postPregledanjeRadovaForm(o, this.formFieldsDto.taskId);
    x.subscribe(
      res => {
        this.tasks = [];
        this.getTasks();
        this.formFields = [];
        this.router.navigate(['/pregledpdfa', this.processInstance]);
      },
      err => {
        console.log("Error occured");
        this.message = "Greska";
      }
    );
  }
}
