import { Component, OnInit } from '@angular/core';
import { AuthService } from '../services/auth/auth-service.service';
import { ObradaService } from '../services/obrada/obrada.service';
import { UserService } from '../services/user/user.service';
import { CasopisService } from '../services/casopis/casopis.service';

@Component({
  selector: 'app-izbor-recenzenata-rada',
  templateUrl: './izbor-recenzenata-rada.component.html',
  styleUrls: ['./izbor-recenzenata-rada.component.css']
})
export class IzborRecenzenataRadaComponent implements OnInit {

  private tasks = [];
  private username = "";
  private taskId = "";
  private formFieldsDto = null;
  private formFields = [];
  public processInstance = "";
  private enumValues = [];
  private message = "";
  private listaRecenzenata = [];
  private listaIzabranihRecenzenta = [];
  private listaSvihRecenzenata = [];
  private naucnaOblastRada = "";

  constructor(private authService : AuthService, private obradaService : ObradaService, private casopisService : CasopisService) { }

  ngOnInit() {
    this.username = this.authService.getUsername();
    this.getTasks();
  }

  getTasks(){
    let x = this.obradaService.getActiveTasksByName(this.username, "Biranje recenzenata");

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

  getIzborRecenzenataForm(taskId : string){
    this.taskId = taskId;
    //console.log(taskId);

    let x = this.obradaService.getIzborRecenzenataForm(taskId);

    x.subscribe(
      res => {
        console.log(res);
        this.formFieldsDto = res;
        this.formFields = res.formFields;
        this.processInstance = res.processInstanceId;
        this.formFields.forEach( (field) =>{
          if(field.type.name=='enum'){
            this.enumValues = Object.keys(field.type.values);
          }
        });

        this.findNaucnuOblastRada();
        this.getRecenzenti();
      },
      err => {
        console.log("Error occured");
      }
    );
  }

  findNaucnuOblastRada(){
    let formField = this.formFields.find(x => x.id == "naucna_oblast_rada");
    if(formField != null){
      this.naucnaOblastRada = formField.defaultValue;
      console.log(this.naucnaOblastRada);
    }
  }

  objChanged(event){
    let pickedValue = event.target.value;
    if(pickedValue == "Svi"){
      this.filtrirajRecenzenteSvi();
    }else if(pickedValue == "Ista Oblast"){
      this.filtrirajRecenzenteIstaOblast();
    }
  }

  filtrirajRecenzenteSvi(){
    this.listaRecenzenata = this.listaSvihRecenzenata;
  }

  filtrirajRecenzenteIstaOblast(){
    let filtrirani = [];
    this.listaRecenzenata.forEach(element => {
      if(element.naucneoblasti.find(x => x.name == this.naucnaOblastRada)){
        filtrirani.push(element);
      }
    });
    this.listaRecenzenata = filtrirani;
  }

  getRecenzenti(){
    let x = this.casopisService.getByTaskId(this.taskId);
    x.subscribe(
      res => {
        console.log(res);
        this.listaRecenzenata = res['recenzenti'];
        this.listaSvihRecenzenata = res['recenzenti'];
      },
      err => {
        console.log("Error occured");
      }
    );
  }

  onSubmit(value, form){

    this.listaIzabranihRecenzenta = [];

    let o = new Array();
    for (var property in value) {
      let recenzent = this.listaRecenzenata.find(x=> x.username == property);
      if(recenzent != null && value[property] == true){
        this.listaIzabranihRecenzenta.push(recenzent.username);
      }else if(recenzent == null){
        o.push({fieldId : property, fieldValue : value[property]});
      }
    }

    o.push({fieldId : "recenzenti", fieldValue : this.listaIzabranihRecenzenta})
    console.log(o);

    this.message = "";
    if(this.listaIzabranihRecenzenta.length < 2){
      this.message = 'Morate izabrati bar dva recenzenta.';
      return;
    }

    let vreme = o.find(x => x.fieldId == "vreme_za_rec");
    if(vreme == null || vreme['fieldValue'] == "" || vreme['fieldValue'] < 1){
      this.message = "Vreme mora biti vece od 1 sekunde.";
      return;
    }

    let x = this.obradaService.postIzborRecenzenataForm(o, this.formFieldsDto.taskId);
    x.subscribe(
      res => {
        this.tasks = [];
        this.getTasks();
        this.formFields = [];
        this.listaRecenzenata = [];
        this.listaIzabranihRecenzenta = [];
        this.listaSvihRecenzenata = [];
        this.message = "Uspesno ste izabrali recenzente.";
      },
      err => {
        console.log("Error occured");
        this.message = "Greska";
      }
    );
  }

}
