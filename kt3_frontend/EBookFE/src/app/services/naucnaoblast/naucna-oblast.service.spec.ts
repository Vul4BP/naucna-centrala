import { TestBed, inject } from '@angular/core/testing';

import { NaucnaOblastService } from './naucna-oblast.service';

describe('NaucnaOblastService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [NaucnaOblastService]
    });
  });

  it('should be created', inject([NaucnaOblastService], (service: NaucnaOblastService) => {
    expect(service).toBeTruthy();
  }));
});
