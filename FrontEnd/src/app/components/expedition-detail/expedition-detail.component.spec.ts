import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ExpeditionDetailComponent } from './expedition-detail.component';

describe('ExpeditionDetailComponent', () => {
  let component: ExpeditionDetailComponent;
  let fixture: ComponentFixture<ExpeditionDetailComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ExpeditionDetailComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ExpeditionDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
