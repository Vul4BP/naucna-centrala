import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth/auth-service.service';
import { RegisterService } from '../services/register/register.service';

@Component({
  selector: 'app-recenzent-proposal',
  templateUrl: './recenzent-proposal.component.html',
  styleUrls: ['./recenzent-proposal.component.css']
})
export class RecenzentProposalComponent implements OnInit {

  private tasks = [];
  private username = "";
  private formFieldsDto = null;
  private formFields = [];
  public processInstance = "";
  private enumValues = [];
  private taskId = "";

  constructor(private authService : AuthService, private registerService : RegisterService, private router: Router) { }

  ngOnInit() {
    this.username = this.authService.getUsername();
    this.getTasks();
  }

  getTasks(){
    let x = this.registerService.getRecenzentProposals(this.username);

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

   getRecenzentiForm(taskId : string){
    this.taskId = taskId;
    console.log(taskId);

    let x = this.registerService.getRecenzentiForm(taskId);

    x.subscribe(
      res => {
        console.log(res);
        //this.categories = res;
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
      //console.log(property);
      //console.log(value[property]);
      o.push({fieldId : property, fieldValue : value[property]});
    }  
    
    console.log(o);
    
    let x = this.registerService.recenzentStatusUser(o, this.formFieldsDto.taskId);

    x.subscribe(
      res => {
        this.tasks = [];
        this.getTasks();
        this.formFields = [];
        //this.router.navigate(['/regresult', this.processInstance]);
      },
      err => {
        console.log("Error occured");
      }
    );
  }
}
