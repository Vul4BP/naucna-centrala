import { Component, OnInit } from '@angular/core';
import { AuthService } from '../services/auth/auth-service.service';
import { ObradaService } from '../services/obrada/obrada.service';

@Component({
  selector: 'app-zahtevi-recenzenata-ispostovani',
  templateUrl: './zahtevi-recenzenata-ispostovani.component.html',
  styleUrls: ['./zahtevi-recenzenata-ispostovani.component.css']
})
export class ZahteviRecenzenataIspostovaniComponent implements OnInit {

  private tasks = [];
  private username = "";
  private taskId = "";
  private formFieldsDto = null;
  private formFields = [];
  public processInstance = "";
  private enumValues = {};
  private message = "";

  constructor(private authService : AuthService, private obradaService : ObradaService) { }

  ngOnInit() {
    this.username = this.authService.getUsername();
    this.getTasks();
  }

  getTasks(){
    let x = this.obradaService.getActiveTasksByName(this.username, "Pregled da li su zahtevi recenzenata ispostovani");

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

  getPregledIzmenaForm(taskId : string){
    this.taskId = taskId;
    console.log(taskId);

    let x = this.obradaService.getPregledIzmenaForm(taskId);

    x.subscribe(
      res => {
        console.log(res);
        this.formFieldsDto = res;
        this.formFields = res.formFields;
        this.processInstance = res.processInstanceId;
        this.formFields.forEach( (field) =>{  
          if(field.type.name=='enum'){
            this.enumValues[field.id] = Object.keys(field.type.values);
          }
        });
        console.log(this.enumValues);

        this.obradaService.getFile(this.processInstance).subscribe(
          response => {
            let blob:any = new Blob([response.body], { type: 'application/pdf; charset=utf-8' });
            const url= window.URL.createObjectURL(blob);
            window.open(url);
          },
          err => {
            console.log(err);
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
      o.push({fieldId : property, fieldValue : value[property]});
    }  
    console.log(o);
    this.message = "";

    if(o.find(x => x.fieldId == "odluka_urednika" && x.fieldValue == "")){
      this.message = "Morate uneti konacnu odluku.";
      return;
    }
    
    let x = this.obradaService.postPregledIzmenaForm(o, this.formFieldsDto.taskId);
    x.subscribe(
      res => {
        this.tasks = [];
        this.getTasks();
        this.formFields = [];
        //this.router.navigate(['/pregledpdfa', this.processInstance]);
        this.message = "Uspesno ste doneli odluku o radu";
      },
      err => {
        console.log("Error occured");
        this.message = "Greska";
      }
    );
  }
}
