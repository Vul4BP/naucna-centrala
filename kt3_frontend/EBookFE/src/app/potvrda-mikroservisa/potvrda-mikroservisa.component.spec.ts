import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PotvrdaMikroservisaComponent } from './potvrda-mikroservisa.component';

describe('PotvrdaMikroservisaComponent', () => {
  let component: PotvrdaMikroservisaComponent;
  let fixture: ComponentFixture<PotvrdaMikroservisaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PotvrdaMikroservisaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PotvrdaMikroservisaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
