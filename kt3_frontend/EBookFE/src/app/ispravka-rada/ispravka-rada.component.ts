import { Component, OnInit } from '@angular/core';
import { AuthService } from '../services/auth/auth-service.service';
import { ObradaService } from '../services/obrada/obrada.service';

@Component({
  selector: 'app-ispravka-rada',
  templateUrl: './ispravka-rada.component.html',
  styleUrls: ['./ispravka-rada.component.css']
})
export class IspravkaRadaComponent implements OnInit {

  private tasks = [];
  private username = "";
  private taskId = "";
  private formFieldsDto = null;
  private formFields = [];
  public processInstance = "";
  private enumValues = {};
  private message = "";
  private fileToUpload: File = null;

  constructor(private authService : AuthService, private obradaService : ObradaService) { }

  ngOnInit() {
    this.username = this.authService.getUsername();
    this.getTasks();
  }

  handleFileInput(files: FileList) {
    this.fileToUpload = files.item(0);
  }

  getTasks(){
    let x = this.obradaService.getActiveTasksByName(this.username, "Ispravljanje rada");

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

  getIspravkaRadovaForm(taskId : string){
    this.taskId = taskId;
    console.log(taskId);

    let x = this.obradaService.getIspravkaRadovaForm(taskId);

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

    this.message = "";
    if(this.fileToUpload == null){
      this.message = "Morate dodati fajl.";
      return;
    }else if(o.find(x => x.fieldId == "odgovor_autora" && x.fieldValue == "")){
      this.message = "Morate uneti odgovor.";
      return;
    }

    let x = this.obradaService.postFileChange(o, this.fileToUpload, this.formFieldsDto.taskId);
    x.subscribe(
      res => {
        console.log(res);
        this.formFields = null;
        this.message = "Uspesno ste izmenuli pdf rada.";
        this.getTasks();
      },
      err => {
        console.log("Error occured");
        this.message = "Validacija nije uspesna.";
      }
    );
  }

}
