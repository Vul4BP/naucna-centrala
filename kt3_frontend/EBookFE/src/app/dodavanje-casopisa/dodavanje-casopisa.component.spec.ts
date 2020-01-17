import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DodavanjeCasopisaComponent } from './dodavanje-casopisa.component';

describe('DodavanjeCasopisaComponent', () => {
  let component: DodavanjeCasopisaComponent;
  let fixture: ComponentFixture<DodavanjeCasopisaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DodavanjeCasopisaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DodavanjeCasopisaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
