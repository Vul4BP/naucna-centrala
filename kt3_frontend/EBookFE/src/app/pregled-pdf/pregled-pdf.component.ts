import { Component, OnInit } from '@angular/core';
import { RepositoryService } from '../services/repository/repository.service';
import { Router, ActivatedRoute } from '@angular/router';
import { ObradaService } from '../services/obrada/obrada.service';
import { AuthService } from '../services/auth/auth-service.service';

@Component({
  selector: 'app-pregled-pdf',
  templateUrl: './pregled-pdf.component.html',
  styleUrls: ['./pregled-pdf.component.css']
})
export class PregledPdfComponent implements OnInit {

  private processInstance = "";
  private tasks = [];
  private username = "";
  private taskId = "";
  private formFieldsDto = null;
  private formFields = [];
  private enumValues = [];
  private message = "";

  private formatiran = true;
  private defaultValue : Number = 0;

  constructor(private authService : AuthService, private route : ActivatedRoute, private obradaService : ObradaService, private router : Router) {
    this.route.params.subscribe(params => {
      this.processInstance = params['id'];
      this.username = authService.getUsername();
      this.getTasks();
    });
   }

  ngOnInit() {
  }

  getTasks(){
    let x = this.obradaService.getActiveTasksByName(this.username,"Pregledanje PDF");
    x.subscribe(
      res => {
        console.log(res);
        this.tasks = res;
        this.taskId = this.tasks[0].taskId;
        this.getPregledanjePdfaForm(this.taskId);
      },
      err => {
        console.log("Error occured");
      }
    );
   }


  getPregledanjePdfaForm(taskId : string){
    this.taskId = taskId;
    console.log(taskId);

    let x = this.obradaService.getPregledanjePdfaForm(taskId);

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
    if(o.find(x => x.fieldId == "komentar" && x.fieldValue == "" && this.formatiran == false)){
      this.message = "Morate uneti komentar";
      return;
    }
    
    let x = this.obradaService.postPregledanjePdfaForm(o, this.formFieldsDto.taskId);
    x.subscribe(
      res => {
        this.tasks = [];
        this.formFields = [];
        //this.getTasks();
        //this.router.navigate(['/pregledpdfa', this.processInstance]);
        this.message = "Uspesno ste izvrsili pregledanje pdfa.";
      },
      err => {
        console.log("Error occured");
        this.message = "Greska";
      }
    );
  }
}
