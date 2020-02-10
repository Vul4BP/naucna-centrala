import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { IzborRecenzenataRadaComponent } from './izbor-recenzenata-rada.component';

describe('IzborRecenzenataRadaComponent', () => {
  let component: IzborRecenzenataRadaComponent;
  let fixture: ComponentFixture<IzborRecenzenataRadaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ IzborRecenzenataRadaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(IzborRecenzenataRadaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
