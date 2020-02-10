import { Component, OnInit } from '@angular/core';
import { AuthService } from '../services/auth/auth-service.service';
import { ObradaService } from '../services/obrada/obrada.service';

@Component({
  selector: 'app-recenzija-rada',
  templateUrl: './recenzija-rada.component.html',
  styleUrls: ['./recenzija-rada.component.css']
})
export class RecenzijaRadaComponent implements OnInit {

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
    let x = this.obradaService.getActiveTasksByName(this.username, "Recenzija rada");

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

  getRecenzijaRadovaForm(taskId : string){
    this.taskId = taskId;
    console.log(taskId);

    let x = this.obradaService.getRecenzijaRadovaForm(taskId);

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

    if(o.find(x => x.fieldId == "preporuka" && x.fieldValue == "")){
      this.message = "Morate uneti preporuku";
      return;
    }else if(o.find(x => x.fieldId == "komentar_za_autora" && x.fieldValue == "")){
      this.message = "Morate uneti komentar za autora";
      return;
    }else if(o.find(x => x.fieldId == "komentar_za_urednika" && x.fieldValue == "")){
      this.message = "Morate uneti komentar za urednika";
      return;
    }
        
    let x = this.obradaService.postRecenzijaRadovaForm(o, this.formFieldsDto.taskId);
    x.subscribe(
      res => {
        this.tasks = [];
        this.getTasks();
        this.formFields = [];
        //this.router.navigate(['/pregledpdfa', this.processInstance]);
        this.message = "Uspesno ste izvrsili recenziju.";
      },
      err => {
        console.log("Error occured");
        this.message = "Greska";
      }
    );
  }

}
