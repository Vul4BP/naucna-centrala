import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PregledCasopisaComponent } from './pregled-casopisa.component';

describe('PregledCasopisaComponent', () => {
  let component: PregledCasopisaComponent;
  let fixture: ComponentFixture<PregledCasopisaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PregledCasopisaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PregledCasopisaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
