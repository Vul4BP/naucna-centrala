import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfirmSubComponent } from './confirm-sub.component';

describe('ConfirmSubComponent', () => {
  let component: ConfirmSubComponent;
  let fixture: ComponentFixture<ConfirmSubComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ConfirmSubComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConfirmSubComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
