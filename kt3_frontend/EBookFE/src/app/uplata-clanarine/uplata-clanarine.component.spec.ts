import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UplataClanarineComponent } from './uplata-clanarine.component';

describe('UplataClanarineComponent', () => {
  let component: UplataClanarineComponent;
  let fixture: ComponentFixture<UplataClanarineComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ UplataClanarineComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UplataClanarineComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
