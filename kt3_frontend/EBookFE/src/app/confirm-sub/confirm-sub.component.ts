import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { ObradaService } from '../services/obrada/obrada.service';
import { AuthService } from '../services/auth/auth-service.service';

@Component({
  selector: 'app-confirm-sub',
  templateUrl: './confirm-sub.component.html',
  styleUrls: ['./confirm-sub.component.css']
})
export class ConfirmSubComponent implements OnInit {

  private username = "";
  private status = "";
  private taskId = "";
  
  constructor(private router: Router, private route: ActivatedRoute, private obradaService: ObradaService, private authService : AuthService) {
    this.route.params.subscribe(params => {
      this.status = params['status'];
      this.username = authService.getUsername();
      let x = obradaService.statusPlacanja(this.status,this.username);
      x.subscribe(
        data => {
          console.log(data);
          if(this.status == "success"){
            this.router.navigate(['/podacioradu', data['id']]);
          }else{
            this.router.navigate(['/uplataclanarine', data['id']]);
          }
      },
        err => {
          console.log(err);
      })
    });
   }

  ngOnInit() {
  }

}
