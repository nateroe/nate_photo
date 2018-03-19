import { TestBed, inject } from '@angular/core/testing';

import { RecentGalleryServiceService } from './recent-gallery-service.service';

describe('RecentGalleryServiceService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [RecentGalleryServiceService]
    });
  });

  it('should be created', inject([RecentGalleryServiceService], (service: RecentGalleryServiceService) => {
    expect(service).toBeTruthy();
  }));
});
