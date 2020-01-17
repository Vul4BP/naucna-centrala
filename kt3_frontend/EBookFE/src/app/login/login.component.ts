import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth/auth-service.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  private password: string = "";
  private username: string = "";
  private message: string = "";

  constructor(private authService: AuthService, private router: Router) { }

  ngOnInit() {
  }

  onSubmit(){
    //console.log(this.username);
    //console.log(this.password);

    this.message = "";

    let jwtAuthenticationRequest = new Object();
    jwtAuthenticationRequest['username'] = this.username;
    jwtAuthenticationRequest['password'] = this.password;

    let x = this.authService.login(jwtAuthenticationRequest);
    x.subscribe(
      res => {
        console.log(res);
        let jwt = res['token'];
        let roles = JSON.stringify(res['authorities']);
        localStorage.setItem('token', jwt);
        localStorage.setItem('roles', roles);
        this.router.navigate(['/userprofile']);
      },
      err => {
        console.log("Error occured");
        this.message = "Username or password incorect.";
      }
    );
  }

}
