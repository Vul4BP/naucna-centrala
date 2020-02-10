import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PregledPdfComponent } from './pregled-pdf.component';

describe('PregledPdfComponent', () => {
  let component: PregledPdfComponent;
  let fixture: ComponentFixture<PregledPdfComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PregledPdfComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PregledPdfComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
