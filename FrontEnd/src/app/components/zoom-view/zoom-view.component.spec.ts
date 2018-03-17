import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ZoomViewComponent } from './zoom-view.component';

describe('ZoomViewComponent', () => {
  let component: ZoomViewComponent;
  let fixture: ComponentFixture<ZoomViewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ZoomViewComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ZoomViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
