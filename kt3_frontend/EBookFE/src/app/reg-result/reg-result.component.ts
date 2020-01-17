import { Component, OnInit, Inject, forwardRef } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { RegisterService } from '../services/register/register.service';
import { RepositoryService } from '../services/repository/repository.service';

@Component({
  selector: 'app-reg-result',
  templateUrl: './reg-result.component.html',
  styleUrls: ['./reg-result.component.css']
})
export class RegResultComponent implements OnInit {
  urlString: string = "";
  url: URL;
  public processInstance = "";
  message: string = "";
  private tasks = [];

  constructor(private registerService : RegisterService, private repositoryService : RepositoryService, private router: Router,private route: ActivatedRoute) {
  }

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.processInstance = params['id'];
      console.log(this.processInstance);
      this.getRegStatus();
    });
  }

  getRegStatus(){
    let x = this.registerService.getRegStatus(this.processInstance);

    x.subscribe(
      res => {
        console.log(res);
        this.message = res['res'];
      },
      err => {
        console.log("Error occured");
      }
    );
   }

   getTasks(){
    let x = this.repositoryService.getTasks(this.processInstance);

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

}
