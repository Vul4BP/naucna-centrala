import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { KorekcijaRadaComponent } from './korekcija-rada.component';

describe('KorekcijaRadaComponent', () => {
  let component: KorekcijaRadaComponent;
  let fixture: ComponentFixture<KorekcijaRadaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ KorekcijaRadaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(KorekcijaRadaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
