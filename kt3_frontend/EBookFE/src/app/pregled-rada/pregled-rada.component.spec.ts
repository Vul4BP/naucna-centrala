import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PregledRadaComponent } from './pregled-rada.component';

describe('PregledRadaComponent', () => {
  let component: PregledRadaComponent;
  let fixture: ComponentFixture<PregledRadaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PregledRadaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PregledRadaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
