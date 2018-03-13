import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SiteLinkComponent } from './site-link.component';

describe('SiteLinkComponent', () => {
  let component: SiteLinkComponent;
  let fixture: ComponentFixture<SiteLinkComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SiteLinkComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SiteLinkComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
