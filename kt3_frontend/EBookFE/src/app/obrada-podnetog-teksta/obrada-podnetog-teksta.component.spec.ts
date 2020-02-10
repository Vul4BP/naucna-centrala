import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ObradaPodnetogTekstaComponent } from './obrada-podnetog-teksta.component';

describe('ObradaPodnetogTekstaComponent', () => {
  let component: ObradaPodnetogTekstaComponent;
  let fixture: ComponentFixture<ObradaPodnetogTekstaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ObradaPodnetogTekstaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ObradaPodnetogTekstaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
