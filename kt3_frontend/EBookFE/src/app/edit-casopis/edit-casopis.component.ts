import { Component, OnInit } from '@angular/core';
import { AuthService } from '../services/auth/auth-service.service';
import { DodavanjeCasopisaService } from '../services/dodavanjecasopisa/dodavanje-casopisa.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-edit-casopis',
  templateUrl: './edit-casopis.component.html',
  styleUrls: ['./edit-casopis.component.css']
})
export class EditCasopisComponent implements OnInit {

  private tasks = [];
  private username = "";

  constructor(private authService : AuthService, private dodavanjeCasopisaService : DodavanjeCasopisaService, private router: Router) { }

  ngOnInit() {
    this.username = this.authService.getUsername();
    this.getTasks();
  }

  getTasks(){
    let x = this.dodavanjeCasopisaService.getActiveTasksByName(this.username, "Popunjavanje forme casopisa");

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

  redirectTo(taskId){
    this.router.navigate(['/dodavanjecasopisa', taskId]);
  }

}
