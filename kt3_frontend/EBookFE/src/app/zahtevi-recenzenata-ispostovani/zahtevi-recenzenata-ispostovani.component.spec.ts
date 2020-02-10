import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ZahteviRecenzenataIspostovaniComponent } from './zahtevi-recenzenata-ispostovani.component';

describe('ZahteviRecenzenataIspostovaniComponent', () => {
  let component: ZahteviRecenzenataIspostovaniComponent;
  let fixture: ComponentFixture<ZahteviRecenzenataIspostovaniComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ZahteviRecenzenataIspostovaniComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ZahteviRecenzenataIspostovaniComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
