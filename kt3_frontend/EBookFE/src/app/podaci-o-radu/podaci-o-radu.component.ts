import { Component, OnInit } from '@angular/core';
import { ObradaService } from '../services/obrada/obrada.service';
import { Router, ActivatedRoute } from '@angular/router';
import { RepositoryService } from '../services/repository/repository.service';

@Component({
  selector: 'app-podaci-o-radu',
  templateUrl: './podaci-o-radu.component.html',
  styleUrls: ['./podaci-o-radu.component.css']
})
export class PodaciORaduComponent implements OnInit {

  private processInstance = "";
  private taskId = "";
  private formFieldsDto = null;
  private formFields = [];
  private enumValues = {};
  private tasks = [];
  private listaIzabranihOblasti = [];
  private listaOblasti = [];
  private message = "";
  private fileToUpload: File = null;
  private koautori = [];

  constructor(private router: Router, private route: ActivatedRoute, private obradaService: ObradaService, private repositoryService : RepositoryService) {
    this.route.params.subscribe(params => {
      this.processInstance = params['id'];
      this.getTaskId();
    });
   }

  ngOnInit() {
  }

  handleFileInput(files: FileList) {
    this.fileToUpload = files.item(0);
  }

  getTaskId(){
    let x = this.repositoryService.getTasks(this.processInstance);

    x.subscribe(
      res => {
        console.log(res);
        this.taskId = res[0]['taskId'];
        
        let y = this.obradaService.getRadForm(this.taskId);

        y.subscribe(
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
          err =>{
            console.log(err);
          }
        )
      },
      err => {
        console.log("Error occured");
      }
    );
   }

   addKoautor(value, form){
    this.message = "";
    let o = {}
    for (var property in value) {
      if(property == "ime_koautora"){
        o['ime'] = value[property];
        form.controls['ime_koautora'].setValue("");
      }
      if(property == "email_koautora"){
        o['email'] = value[property];
        form.controls['email_koautora'].setValue("");
      }
      if(property == "grad_koautora"){
        o['grad'] = value[property];
        form.controls['grad_koautora'].setValue("");
      }
      if(property == "drzava_koautora"){
        o['drzava'] = value[property];
        form.controls['drzava_koautora'].setValue("");
      }
    }
    console.log(o);
    if(o["ime"] == "" || o["email"] == "" || o["grad"] == "" || o["drzava"] == ""){
      console.log("Morate popunite sve podatke o koautoru");
      this.message = "Morate popuniti sve podatke o koautoru";
      return;
    }
    this.koautori.push(o);
   }

   onSubmit(value, form){
    let o = new Array();
    for (var property in value) {
      if(property != "ime_koautora" && property != "email_koautora" && property != "grad_koautora" && property != "drzava_koautora"){
        o.push({fieldId : property, fieldValue : value[property]});
      }
    }
    o.push({fieldId : "koautori", fieldValue : this.koautori});
    console.log(o);

    this.message = "";

    let x = this.obradaService.postFile(o, this.fileToUpload, this.formFieldsDto.taskId);
    x.subscribe(
      res => {
        console.log(res);
        this.formFields = null;
        this.message = "Uspesno ste podneli zahtev za dodavanje rada.";
      },
      err => {
        console.log("Error occured");
        this.message = "Validacija nije uspesna.";
      }
    );
  }

}
