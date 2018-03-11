import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BestOfGalleryComponent } from './best-of-gallery.component';

describe('BestOfGalleryComponent', () => {
  let component: BestOfGalleryComponent;
  let fixture: ComponentFixture<BestOfGalleryComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BestOfGalleryComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BestOfGalleryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
