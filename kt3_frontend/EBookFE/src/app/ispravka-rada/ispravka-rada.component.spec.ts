import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { IspravkaRadaComponent } from './ispravka-rada.component';

describe('IspravkaRadaComponent', () => {
  let component: IspravkaRadaComponent;
  let fixture: ComponentFixture<IspravkaRadaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ IspravkaRadaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(IspravkaRadaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
