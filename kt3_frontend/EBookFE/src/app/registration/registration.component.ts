import { Component, OnInit, Input } from '@angular/core';
import { Router } from '@angular/router';
import { RepositoryService } from '../services/repository/repository.service';
import { RegisterService } from '../services/register/register.service';
import { NaucnaOblastService } from '../services/naucnaoblast/naucna-oblast.service';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent implements OnInit {

  private formFieldsDto = null;
  private formFields = [];
  public processInstance = "";
  private enumValues = {};
  private tasks = [];
  private listaIzabranihOblasti = [];
  private listaOblasti = [];
  private message = "";

  constructor(private naucnaOblastService : NaucnaOblastService, private registerService : RegisterService,
     private repositoryService : RepositoryService, private router: Router) {

    let x = registerService.startProcess();
    x.subscribe(
      res => {
        console.log(res);
        this.formFieldsDto = res;
        this.formFields = res.formFields;
        this.processInstance = res.processInstanceId;
        this.formFields.forEach( (field) =>{  
          if( field.type.name=='enum'){
            this.enumValues[field.id] = Object.keys(field.type.values);
          }
        });

        let y = naucnaOblastService.getAll();
        y.subscribe(
          res => {
            console.log(res);
            this.listaOblasti = res;
          }
        )
      },
      err => {
        console.log("Error occured");
        this.message = "Desila se greska.";
      }
    );
   }

  ngOnInit() {
  }

  onSubmit(value, form){

    this.listaIzabranihOblasti = [];

    let o = new Array();
    for (var property in value) {
      let oblast = this.listaOblasti.find(x=> x.name == property);
      if(oblast != null && value[property] == true){
        this.listaIzabranihOblasti.push(oblast);
      }else if(oblast == null){
        o.push({fieldId : property, fieldValue : value[property]});
      }
    }

    o.push({fieldId : "oblasti", fieldValue : this.listaIzabranihOblasti})
    console.log(o);
    //validacija sa klijentske strane
    
    this.message = "";
    if(this.listaIzabranihOblasti.length == 0){
      this.message = 'Morate izabrati bar jednu naucnu oblast.';
      return;
    }

    let x = this.registerService.registerUser(o, this.formFieldsDto.taskId);

    x.subscribe(
      res => {
        //console.log(res);
        //alert("You registered successfully!")
        this.router.navigate(['/regresult', this.processInstance]);
      },
      err => {
        console.log("Error occured");
        this.message = "Validacija nije uspesna.";
        this.getTaskId();
      }
    );
  }

  getTaskId(){
    let x = this.repositoryService.getTasks(this.processInstance);

    x.subscribe(
      res => {
        console.log(res);
        this.tasks = res;
        this.formFieldsDto.taskId = res[0]['taskId'];
      },
      err => {
        console.log("Error occured");
      }
    );
   }

  getTasks(){
    let x = this.repositoryService.getTasks(this.processInstance);

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

   claim(taskId){
    let x = this.repositoryService.claimTask(taskId);

    x.subscribe(
      res => {
        console.log(res);
      },
      err => {
        console.log("Error occured");
      }
    );
   }

   complete(taskId){
    let x = this.repositoryService.completeTask(taskId);

    x.subscribe(
      res => {
        console.log(res);
        this.tasks = res;
      },
      err => {
        console.log("Error occured");
      }
    );
   }

}
