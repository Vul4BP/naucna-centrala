import { TestBed } from '@angular/core/testing';

import { ObradaService } from './obrada.service';

describe('ObradaService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: ObradaService = TestBed.get(ObradaService);
    expect(service).toBeTruthy();
  });
});
