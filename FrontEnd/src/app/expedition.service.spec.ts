import { TestBed, inject } from '@angular/core/testing';

import { ExpeditionService } from './expedition.service';

describe('ExpeditionService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ExpeditionService]
    });
  });

  it('should be created', inject([ExpeditionService], (service: ExpeditionService) => {
    expect(service).toBeTruthy();
  }));
});
