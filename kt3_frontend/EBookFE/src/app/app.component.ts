import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from './services/auth/auth-service.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'app';

  constructor(private authService : AuthService, private router: Router) { }

  logout(){
    let res = this.authService.logout();
    if(res == true){
      console.log("Logout success");
      this.router.navigate(['/']);
    }else{
      console.log("Error occured");
    }
  }

}
