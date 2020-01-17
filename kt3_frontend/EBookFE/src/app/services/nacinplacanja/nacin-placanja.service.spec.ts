import { TestBed, inject } from '@angular/core/testing';

import { NacinPlacanjaService } from './nacin-placanja.service';

describe('NacinPlacanjaService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [NacinPlacanjaService]
    });
  });

  it('should be created', inject([NacinPlacanjaService], (service: NacinPlacanjaService) => {
    expect(service).toBeTruthy();
  }));
});
