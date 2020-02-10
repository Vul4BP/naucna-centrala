import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RecenzijaRadaComponent } from './recenzija-rada.component';

describe('RecenzijaRadaComponent', () => {
  let component: RecenzijaRadaComponent;
  let fixture: ComponentFixture<RecenzijaRadaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RecenzijaRadaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RecenzijaRadaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
