import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ExpeditionHeaderComponent } from './expedition-header.component';

describe('ExpeditionHeaderComponent', () => {
  let component: ExpeditionHeaderComponent;
  let fixture: ComponentFixture<ExpeditionHeaderComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ExpeditionHeaderComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ExpeditionHeaderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
