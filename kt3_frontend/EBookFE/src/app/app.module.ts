import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule }   from '@angular/forms';
import { RouterModule, Routes } from '@angular/router';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { HttpModule } from '@angular/http';

import { AppComponent } from './app.component';
import { RegistrationComponent } from './registration/registration.component';

import {Authorized} from './guard/authorized.guard';
import {Admin} from './guard/admin.guard';
import {Notauthorized} from './guard/notauthorized.guard';
import { RegResultComponent } from './reg-result/reg-result.component';
import { LoginComponent } from './login/login.component';
import { UserProfileComponent } from './user-profile/user-profile.component';
import { RecenzentProposalComponent } from './recenzent-proposal/recenzent-proposal.component';
import { TokenInterceptor } from './token.interceptor';
import { DodavanjeCasopisaComponent } from './dodavanje-casopisa/dodavanje-casopisa.component';
import { Urednik } from './guard/urendik.guard';
import { DodavanjeRecIUredComponent } from './dodavanje-rec-i-ured/dodavanje-rec-i-ured.component';
import { PregledCasopisaComponent } from './pregled-casopisa/pregled-casopisa.component';
import { EditCasopisComponent } from './edit-casopis/edit-casopis.component';

const Routes = [
  {
    path: "registrate",
    component: RegistrationComponent,
    canActivate: [Notauthorized]
  },
  {
    path: "regresult/:id",
    component: RegResultComponent,
    canActivate: [Notauthorized]
  },
  {
    path: "login",
    component: LoginComponent,
    canActivate: [Notauthorized]
  },
  {
    path: "userprofile",
    component: UserProfileComponent,
    canActivate: [Authorized]
  },
  {
    path: "recenzenti",
    component: RecenzentProposalComponent,
    canActivate: [Admin]
  },
  {
    path: "casopisi",
    component: PregledCasopisaComponent,
    canActivate: [Admin]
  },
  {
    path: "dodavanjecasopisa",
    component: DodavanjeCasopisaComponent,
    canActivate: [Urednik]
  },
  {
    path: "dodavanjecasopisa/:id",
    component: DodavanjeCasopisaComponent,
    canActivate: [Urednik]
  },
  {
    path: "dodavanjereciured",
    component: DodavanjeRecIUredComponent,
    canActivate: [Urednik]
  },
  {
    path: "editcasopis",
    component: EditCasopisComponent,
    canActivate: [Urednik]
  }
]

@NgModule({
  declarations: [
    AppComponent,
    RegistrationComponent,
    RegResultComponent,
    LoginComponent,
    UserProfileComponent,
    RecenzentProposalComponent,
    DodavanjeCasopisaComponent,
    DodavanjeRecIUredComponent,
    PregledCasopisaComponent,
    EditCasopisComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    RouterModule.forRoot(Routes),
    HttpClientModule, 
    HttpModule
  ],
  
  providers:  [
    Admin,
    Authorized,
    Notauthorized,
    Urednik,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: TokenInterceptor,
      multi: true
    }
    ],
  bootstrap: [AppComponent]
})
export class AppModule { }
