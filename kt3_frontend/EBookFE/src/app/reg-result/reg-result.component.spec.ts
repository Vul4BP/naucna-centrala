import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RegResultComponent } from './reg-result.component';

describe('RegResultComponent', () => {
  let component: RegResultComponent;
  let fixture: ComponentFixture<RegResultComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RegResultComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RegResultComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
