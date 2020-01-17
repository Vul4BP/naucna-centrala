import { TestBed, inject } from '@angular/core/testing';

import { DodavanjeCasopisaService } from './dodavanje-casopisa.service';

describe('DodavanjeCasopisaService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [DodavanjeCasopisaService]
    });
  });

  it('should be created', inject([DodavanjeCasopisaService], (service: DodavanjeCasopisaService) => {
    expect(service).toBeTruthy();
  }));
});
