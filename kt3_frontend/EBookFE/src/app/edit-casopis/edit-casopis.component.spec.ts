import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EditCasopisComponent } from './edit-casopis.component';

describe('EditCasopisComponent', () => {
  let component: EditCasopisComponent;
  let fixture: ComponentFixture<EditCasopisComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EditCasopisComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EditCasopisComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
