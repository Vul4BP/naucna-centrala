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
import { Urednik } from './guard/urednik.guard';
import { DodavanjeRecIUredComponent } from './dodavanje-rec-i-ured/dodavanje-rec-i-ured.component';
import { PregledCasopisaComponent } from './pregled-casopisa/pregled-casopisa.component';
import { EditCasopisComponent } from './edit-casopis/edit-casopis.component';
import { PotvrdaMikroservisaComponent } from './potvrda-mikroservisa/potvrda-mikroservisa.component';
import { AllMagazinesComponent } from './all-magazines/all-magazines.component';
import { Obican } from './guard/obican.guard';
import { Autor } from './guard/autor.guard';
import { ObradaPodnetogTekstaComponent } from './obrada-podnetog-teksta/obrada-podnetog-teksta.component';
import { UplataClanarineComponent } from './uplata-clanarine/uplata-clanarine.component';
import { ConfirmSubComponent } from './confirm-sub/confirm-sub.component';
import { PodaciORaduComponent } from './podaci-o-radu/podaci-o-radu.component';
import { PregledRadaComponent } from './pregled-rada/pregled-rada.component';
import { PregledPdfComponent } from './pregled-pdf/pregled-pdf.component';
import { KorekcijaRadaComponent } from './korekcija-rada/korekcija-rada.component';
import { IzborRecenzenataRadaComponent } from './izbor-recenzenata-rada/izbor-recenzenata-rada.component';
import { RecenzijaRadaComponent } from './recenzija-rada/recenzija-rada.component';
import { Recenzent } from './guard/recenzent.guard';
import { IzborNovogRecenzentaComponent } from './izbor-novog-recenzenta/izbor-novog-recenzenta.component';
import { CitanjeKomentaraComponent } from './citanje-komentara/citanje-komentara.component';
import { IspravkaRadaComponent } from './ispravka-rada/ispravka-rada.component';
import { AuthService } from './services/auth/auth-service.service';
import { ZahteviRecenzenataIspostovaniComponent } from './zahtevi-recenzenata-ispostovani/zahtevi-recenzenata-ispostovani.component';

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
  },
  {
    path: "potvrdinacineplacanja",
    component: PotvrdaMikroservisaComponent,
    canActivate: [Urednik]
  },
  {
    path: "kupicasopise",
    component: AllMagazinesComponent,
    canActivate: [Obican]
  },
  {
    path: "obradapodnetogteksta",
    component: ObradaPodnetogTekstaComponent,
    canActivate: [Autor]
  },
  {
    path: "uplataclanarine/:id",
    component: UplataClanarineComponent,
    canActivate: [Autor]
  },
  {
    path: "confirmsub/:status",
    component: ConfirmSubComponent,
    canActivate: [Autor]
  },
  {
    path: "podacioradu/:id",
    component: PodaciORaduComponent,
    canActivate: [Autor]
  },
  {
    path: "pregledrada",
    component: PregledRadaComponent,
    canActivate: [Urednik]
  },
  {
    path: "pregledpdfa/:id",
    component: PregledPdfComponent,
    canActivate: [Urednik]
  },
  {
    path: "korekcijarada",
    component: KorekcijaRadaComponent,
    canActivate: [Autor]
  },
  {
    path: "biranjerecenzenatarada",
    component: IzborRecenzenataRadaComponent,
    canActivate: [Urednik]
  },
  {
    path: "recenzijarada",
    component: RecenzijaRadaComponent,
    canActivate: [Recenzent]
  },
  {
    path: "izbornovogrecenzenta",
    component: IzborNovogRecenzentaComponent,
    canActivate: [Urednik]
  },
  {
    path: "citanjekomentara",
    component: CitanjeKomentaraComponent,
    canActivate: [Urednik]
  },
  {
    path: "ispravkarada",
    component: IspravkaRadaComponent,
    canActivate: [Autor]
  },
  {
    path: "zahtevirecenzenatavalidni",
    component: ZahteviRecenzenataIspostovaniComponent,
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
    EditCasopisComponent,
    PotvrdaMikroservisaComponent,
    AllMagazinesComponent,
    ObradaPodnetogTekstaComponent,
    UplataClanarineComponent,
    ConfirmSubComponent,
    PodaciORaduComponent,
    PregledRadaComponent,
    PregledPdfComponent,
    KorekcijaRadaComponent,
    IzborRecenzenataRadaComponent,
    RecenzijaRadaComponent,
    IzborNovogRecenzentaComponent,
    CitanjeKomentaraComponent,
    IspravkaRadaComponent,
    ZahteviRecenzenataIspostovaniComponent
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
    Obican,
    Autor,
    Recenzent,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: TokenInterceptor,
      multi: true
    }
    ],
  bootstrap: [AppComponent]
})
export class AppModule { }
