import { Component, OnInit } from '@angular/core';
import { AuthService } from '../services/auth/auth-service.service';
import { DodavanjeCasopisaService } from '../services/dodavanjecasopisa/dodavanje-casopisa.service';
import { Router } from '@angular/router';
import { UserService } from '../services/user/user.service';
import { RepositoryService } from '../services/repository/repository.service';

@Component({
  selector: 'app-dodavanje-rec-i-ured',
  templateUrl: './dodavanje-rec-i-ured.component.html',
  styleUrls: ['./dodavanje-rec-i-ured.component.css']
})
export class DodavanjeRecIUredComponent implements OnInit {

  private tasks = [];
  private username = "";
  private formFieldsDto = null;
  private formFields = [];
  public processInstance = "";
  private enumValues = [];
  private taskId = "";
  private listaUrednika = [];
  private listaIzabranihUrednika = [];
  private listaRecenzenata = [];
  private listaIzabranihRecenzenta = [];
  private message = "";

  constructor(private userService : UserService, private authService : AuthService, private dodavanjeCasopisaService : DodavanjeCasopisaService, 
    private repositoryService : RepositoryService, private router: Router) { }

  ngOnInit() {
    this.username = this.authService.getUsername();
    this.getTasks();
  }

  getTasks(){
    let x = this.dodavanjeCasopisaService.getActiveTasksByName(this.username, "Dodaj urednike i recenzente");

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

  getDodavanjeUrednikaIRecenzenataForm(taskId : string){
    this.taskId = taskId;
    console.log(taskId);

    let x = this.dodavanjeCasopisaService.getDodavanjeUrednikaIRecenzenataForm(taskId);

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
        this.getRecenzentiIUrednici();
      },
      err => {
        console.log("Error occured");
      }
    );
  }

  getRecenzentiIUrednici(){
    let x = this.userService.getAllByRole("ROLE_RECENZENT");
    x.subscribe(
      res => {
        console.log(res);
        this.listaRecenzenata = res;
        this.getUrednici();
      },
      err => {
        console.log("Error occured");
      }
    );
  }

  getUrednici(){      
    //console.log(res);
    //alert("You registered successfully!")
    //this.router.navigate(['/regresult', this.processInstance]);
    let x = this.userService.getAllByRole("ROLE_UREDNIK");

    x.subscribe(
      res => {
        console.log(res);
        this.listaUrednika = res;
      },
      err => {
        console.log("Error occured");
      }
    );
  }

  onSubmit(value, form){
    this.listaIzabranihUrednika = [];
    this.listaIzabranihRecenzenta = [];

    let o = new Array();
    for (var property in value) {
      let urednik = this.listaUrednika.find(x=> x.username == property);
      let recenzent = this.listaRecenzenata.find(x=> x.username == property);
      if(urednik != null && value[property] == true){
        this.listaIzabranihUrednika.push(urednik.username);
      }else if(recenzent != null && value[property] == true){
        this.listaIzabranihRecenzenta.push(recenzent.username);
      }else if(urednik == null && recenzent == null){
        o.push({fieldId : property, fieldValue : value[property]});
      }
    }

    o.push({fieldId : "recenzenti", fieldValue : this.listaIzabranihRecenzenta})
    o.push({fieldId : "urednici", fieldValue : this.listaIzabranihUrednika})
    console.log(o);

    this.message = "";
    if(this.listaIzabranihRecenzenta.length < 2){
      this.message = 'Morate izabrati bar dva recenzenta.';
      return;
    }
    
    let x = this.dodavanjeCasopisaService.dodajUrednikeIRecenzente(o, this.formFieldsDto.taskId);

    x.subscribe(
      res => {
        this.tasks = [];
        this.getTasks();
        this.formFields = [];
        this.formFieldsDto = [];
        this.listaRecenzenata = [];
        this.listaUrednika = [];
        //this.router.navigate(['/regresult', this.processInstance]);
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
        this.claim(this.formFieldsDto.taskId);
        //this.getDodavanjeUrednikaIRecenzenataForm(this.formFieldsDto.taskId);
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
        this.getDodavanjeUrednikaIRecenzenataForm(this.formFieldsDto.taskId);
      },
      err => {
        console.log("Error occured");
      }
    );
   }

}
