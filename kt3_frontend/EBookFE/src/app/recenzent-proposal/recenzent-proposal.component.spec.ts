import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RecenzentProposalComponent } from './recenzent-proposal.component';

describe('RecenzentProposalComponent', () => {
  let component: RecenzentProposalComponent;
  let fixture: ComponentFixture<RecenzentProposalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RecenzentProposalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RecenzentProposalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
