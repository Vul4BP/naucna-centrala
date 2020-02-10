import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CitanjeKomentaraComponent } from './citanje-komentara.component';

describe('CitanjeKomentaraComponent', () => {
  let component: CitanjeKomentaraComponent;
  let fixture: ComponentFixture<CitanjeKomentaraComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CitanjeKomentaraComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CitanjeKomentaraComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
