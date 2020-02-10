import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { RepositoryService } from '../services/repository/repository.service';
import { CasopisService } from '../services/casopis/casopis.service';

@Component({
  selector: 'app-uplata-clanarine',
  templateUrl: './uplata-clanarine.component.html',
  styleUrls: ['./uplata-clanarine.component.css']
})
export class UplataClanarineComponent implements OnInit {

  private tasks = [];
  private message = "";
  private taskId = "";
  private processInstance = "";
  private casopis = Object;

  constructor(private repositoryService : RepositoryService, private router: Router,private route: ActivatedRoute, private casopisService : CasopisService) { 
    this.route.params.subscribe(params => {
      this.processInstance = params['id'];
      this.getTasks();
    });
  }

  ngOnInit() {
  }

  getTasks(){
    let x = this.repositoryService.getTasks(this.processInstance);
    x.subscribe(
      res => {
        console.log(res);
        this.tasks = res;
        this.taskId = this.tasks[0].taskId;
        
        let x = this.casopisService.getByTaskId(this.taskId)
        x.subscribe(
          res => {
            console.log(res);
            this.casopis = res;
          },
          err => {
            console.log("Error occured");
          }
        );

      },
      err => {
        console.log("Error occured");
      }
    );
   }

   checkIfPaypal(id : Number) : boolean{
    let res = false;
    if(this.casopis != null && this.casopis['naciniPlacanja'].find(x => x.name == "Paypal") != null){
      res = true;
    }
    return res;
  }

  subscribeCasopis(id : Number){
    //console.log(id);
    this.casopisService.subscribeCasopis(id)
    .subscribe(
      data => {
        console.log(data);
        window.location.replace(data['redirectUrl']);
      },
      error => {
        console.log("Error");
      })
  }
}
