import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DodavanjeRecIUredComponent } from './dodavanje-rec-i-ured.component';

describe('DodavanjeRecIUredComponent', () => {
  let component: DodavanjeRecIUredComponent;
  let fixture: ComponentFixture<DodavanjeRecIUredComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DodavanjeRecIUredComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DodavanjeRecIUredComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
