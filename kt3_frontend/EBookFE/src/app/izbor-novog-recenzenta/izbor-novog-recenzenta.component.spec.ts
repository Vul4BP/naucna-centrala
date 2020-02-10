import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { IzborNovogRecenzentaComponent } from './izbor-novog-recenzenta.component';

describe('IzborNovogRecenzentaComponent', () => {
  let component: IzborNovogRecenzentaComponent;
  let fixture: ComponentFixture<IzborNovogRecenzentaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ IzborNovogRecenzentaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(IzborNovogRecenzentaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
