import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ExpeditionGalleryComponent } from './expedition-gallery.component';

describe('ExpeditionGalleryComponent', () => {
  let component: ExpeditionGalleryComponent;
  let fixture: ComponentFixture<ExpeditionGalleryComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ExpeditionGalleryComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ExpeditionGalleryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
