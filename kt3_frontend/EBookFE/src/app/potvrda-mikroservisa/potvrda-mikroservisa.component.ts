import { Component, OnInit } from '@angular/core';
import { AuthService } from '../services/auth/auth-service.service';
import { DodavanjeCasopisaService } from '../services/dodavanjecasopisa/dodavanje-casopisa.service';
import { Router } from '@angular/router';
import { UserService } from '../services/user/user.service';
import { RepositoryService } from '../services/repository/repository.service';


@Component({
  selector: 'app-potvrda-mikroservisa',
  templateUrl: './potvrda-mikroservisa.component.html',
  styleUrls: ['./potvrda-mikroservisa.component.css']
})
export class PotvrdaMikroservisaComponent implements OnInit {

  private tasks = [];
  private username = "";
  private formFields = null;
  public processInstance = "";
  private taskId = "";

  constructor(private userService : UserService, private authService : AuthService, private dodavanjeCasopisaService : DodavanjeCasopisaService, 
    private repositoryService : RepositoryService, private router: Router) { }

  ngOnInit() {
    this.username = this.authService.getUsername();
    this.getTasks();
  }

  getTasks(){
    let x = this.dodavanjeCasopisaService.getActiveTasksByName(this.username, "Podaci za mikroservise");

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

  getForm(taskId : string){
    this.taskId = taskId;
    console.log(taskId);

    let x = this.dodavanjeCasopisaService.getPotvrdaMikroservisa(taskId);

    x.subscribe(
      res => {
        console.log(res);
        this.formFields = res;
      },
      err => {
        console.log("Error occured");
      }
    );
  }

  onSubmit(value, form){
    let o = new Array();

    let x = this.dodavanjeCasopisaService.proveraPotvrdjenihMikroservisa(o, this.taskId);
    x.subscribe(
      res => {
        this.tasks = [];
        this.getTasks();
        this.formFields = null;
        //this.router.navigate(['/regresult', this.processInstance]);
      },
      err => {
        console.log("Error occured");
        //this.message = "Validacija nije uspesna.";
        //this.getTaskId();
      }
    );
  }
}
