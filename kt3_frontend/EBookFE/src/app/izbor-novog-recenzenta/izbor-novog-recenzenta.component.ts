import { Component, OnInit } from '@angular/core';
import { ObradaService } from '../services/obrada/obrada.service';
import { AuthService } from '../services/auth/auth-service.service';

@Component({
  selector: 'app-izbor-novog-recenzenta',
  templateUrl: './izbor-novog-recenzenta.component.html',
  styleUrls: ['./izbor-novog-recenzenta.component.css']
})
export class IzborNovogRecenzentaComponent implements OnInit {

  private tasks = [];
  private username = "";
  private taskId = "";
  private formFieldsDto = null;
  private formFields = [];
  public processInstance = "";
  private enumValues = [];
  private message = "";

  constructor(private authService : AuthService, private obradaService : ObradaService) { }

  ngOnInit() {
    this.username = this.authService.getUsername();
    this.getTasks();
  }

  getTasks(){
    let x = this.obradaService.getMultipleActiveTasksByName(this.username, "Izbor novog recenzenata");

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

  getIzborNovogRecenzentaForm(taskId : string){
    this.taskId = taskId;
    console.log(taskId);

    let x = this.obradaService.getIzborNovogRecenzentaForm(taskId);

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

    if(o.find(x => x.fieldId == "novi_recenzent" && x.fieldValue == "")){
      this.message = "Morate uneti novog recenzenta.";
      return;
    }else if(o.find(x => x.fieldId == "vreme_za_rec" && x.fieldValue == "")){
      this.message = "Morate uneti vreme u sekundama.";
      return;
    }
    
    let x = this.obradaService.postIzborNovogRecenzentaForm(o, this.formFieldsDto.taskId);
    x.subscribe(
      res => {
        this.tasks = [];
        this.getTasks();
        this.formFields = [];
        //this.router.navigate(['/pregledpdfa', this.processInstance]);
        this.message = "Uspesno ste zamenuli recenzenta";
      },
      err => {
        console.log("Error occured");
        this.message = "Greska";
      }
    );
  }

}
