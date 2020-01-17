import { Component, OnInit } from '@angular/core';
import { DodavanjeCasopisaService } from '../services/dodavanjecasopisa/dodavanje-casopisa.service';
import { RepositoryService } from '../services/repository/repository.service';
import { Router, ActivatedRoute } from '@angular/router';
import { AuthService } from '../services/auth/auth-service.service';
import { NaucnaOblastService } from '../services/naucnaoblast/naucna-oblast.service';
import { NacinPlacanjaService } from '../services/nacinplacanja/nacin-placanja.service';
import { isNumber } from 'util';

@Component({
  selector: 'app-dodavanje-casopisa',
  templateUrl: './dodavanje-casopisa.component.html',
  styleUrls: ['./dodavanje-casopisa.component.css']
})
export class DodavanjeCasopisaComponent implements OnInit {

  private formFieldsDto = null;
  private formFields = [];
  public processInstance = "";
  private enumValues = {};
  private tasks = [];
  private listaIzabranihOblasti = [];
  private listaOblasti = [];
  private listaIzabranihPlacanja = [];
  private listaPlacanja = [];
  private message = "";
  private taskId = "";

  constructor(private authService : AuthService, private naucnaOblastService : NaucnaOblastService, 
      private dodavanjeCasopisaService : DodavanjeCasopisaService, private repositoryService : RepositoryService,
      private nacinPlacanjaService : NacinPlacanjaService ,private router: Router, private route: ActivatedRoute) {

    this.route.params.subscribe(params => {
      this.taskId = params['id'];
      let x;
      if(this.taskId != null){
        x = dodavanjeCasopisaService.getDodavanjeCasopisaForm(this.taskId);
      }else{
        let username = authService.getUsername();
        x = dodavanjeCasopisaService.startProcess(username);
      }
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
  
              let z = nacinPlacanjaService.getAll();
              z.subscribe(
                res => {
                  console.log(res);
                  this.listaPlacanja = res;
                }
              );
            }
          );
        },
        err => {
          console.log("Error occured");
          this.message = "Desila se greska.";
        }
      );
     });
        
    /*
    let username = authService.getUsername();
    let x = dodavanjeCasopisaService.startProcess(username);
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

            let z = nacinPlacanjaService.getAll();
            z.subscribe(
              res => {
                console.log(res);
                this.listaPlacanja = res;
              }
            );
          }
        );
      },
      err => {
        console.log("Error occured");
        this.message = "Desila se greska.";
      }
    );
    */
  }

  ngOnInit() {
  }

  onSubmit(value, form){

    this.listaIzabranihOblasti = [];
    this.listaIzabranihPlacanja = [];

    let o = new Array();
    for (var property in value) {
      let oblast = this.listaOblasti.find(x=> x.name == property);
      let placanje = this.listaPlacanja.find(x=> x.name == property);
      if(oblast != null && value[property] == true){
        this.listaIzabranihOblasti.push(oblast);
      }else if(placanje != null && value[property] == true){
        this.listaIzabranihPlacanja.push(placanje);
      }else if(oblast == null && placanje == null){
        o.push({fieldId : property, fieldValue : value[property]});
      }
    }

    o.push({fieldId : "oblasti", fieldValue : this.listaIzabranihOblasti})
    o.push({fieldId : "placanja", fieldValue : this.listaIzabranihPlacanja})
    console.log(o);
    //validacija sa klijentske strane
    
    this.message = "";
    if(this.listaIzabranihOblasti.length == 0){
      this.message = 'Morate izabrati bar jednu naucnu oblast.';
      return;
    }

    if(this.listaIzabranihPlacanja.length == 0){
      this.message = 'Morate izabrati bar jedan nacin placanja.';
      return;
    }

    if(o.find(x => x.fieldId == "komeSePlaca")['fieldValue'] == ""){
      this.message = 'Morate izabrati kome se placa.';
      return;
    }

    if(isNumber(o.find(x => x.fieldId == "clanarina")['fieldValue']) == false){
      this.message = 'Clanarina mora da predstavlja broj.';
      return;
    }

    let x = this.dodavanjeCasopisaService.addCasopis(o, this.formFieldsDto.taskId);

    x.subscribe(
      res => {
        //console.log(res);
        //alert("You registered successfully!")
        //this.router.navigate(['/regresult', this.processInstance]);
        this.getTaskIdSuccess();
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

   getTaskIdSuccess(){
    let x = this.repositoryService.getTasks(this.processInstance);

    x.subscribe(
      res => {
        console.log(res);
        this.tasks = res;
        this.claim(res[0]['taskId']);
        //this.router.navigate(['/dodavanjereciured']);
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
    //console.log("TASK ID FOR CLAIM: " + taskId);
    let x = this.repositoryService.claimTask(taskId);

    x.subscribe(
      res => {
        console.log(res);
        this.router.navigate(['/dodavanjereciured']);
      },
      err => {
        console.log("Error occured");
      }
    );
   }
}
