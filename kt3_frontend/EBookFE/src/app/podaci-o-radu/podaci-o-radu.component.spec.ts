import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PodaciORaduComponent } from './podaci-o-radu.component';

describe('PodaciORaduComponent', () => {
  let component: PodaciORaduComponent;
  let fixture: ComponentFixture<PodaciORaduComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PodaciORaduComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PodaciORaduComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
