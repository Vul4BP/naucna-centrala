import { Component, OnInit } from '@angular/core';
import { AuthService } from '../services/auth/auth-service.service';
import { ObradaService } from '../services/obrada/obrada.service';

@Component({
  selector: 'app-citanje-komentara',
  templateUrl: './citanje-komentara.component.html',
  styleUrls: ['./citanje-komentara.component.css']
})
export class CitanjeKomentaraComponent implements OnInit {

  private tasks = [];
  private username = "";
  private taskId = "";
  private formFieldsDto = null;
  private formFields = [];
  public processInstance = "";
  private enumValues = {};
  private message = "";

  private timeValue = 0;

  constructor(private authService : AuthService, private obradaService : ObradaService) { }

  ngOnInit() {
    this.username = this.authService.getUsername();
    this.getTasks();
  }

  getTasks(){
    let x = this.obradaService.getActiveTasksByName(this.username, "Vracanje rada uredniku na obradu");

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

  getCitanjeKomentaraForm(taskId : string){
    this.taskId = taskId;
    console.log(taskId);

    let x = this.obradaService.getCitanjeKomentaraForm(taskId);

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

    if(o.find(x => x.fieldId == "konacna_odluka" && x.fieldValue == "")){
      this.message = "Morate uneti konacnu odluku.";
      return;
    }else if(o.find(x => x.fieldId == "vreme_za_ispravku" && x.fieldValue == "")){
      let odluka = o.find(x => x.fieldId == "konacna_odluka");
      if(odluka['fieldValue'] == "manje_ispravke" || odluka['fieldValue'] == "uslovno_prihvatiti"){
        this.message = "Morate uneti vreme u sekundama.";
        return;
      }
    }
    
    let x = this.obradaService.postCitanjeKomentaraForm(o, this.formFieldsDto.taskId);
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
