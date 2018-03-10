import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PhotoDetailComponent } from './photo_detail.component';

describe( 'PhotoComponent', () => {
    let component: PhotoDetailComponent;
    let fixture: ComponentFixture<PhotoDetailComponent>;

    beforeEach( async(() => {
        TestBed.configureTestingModule( {
            declarations: [PhotoDetailComponent]
        } )
            .compileComponents();
    } ) );

    beforeEach(() => {
        fixture = TestBed.createComponent( PhotoDetailComponent );
        component = fixture.componentInstance;
        fixture.detectChanges();
    } );

    it( 'should create', () => {
        expect( component ).toBeTruthy();
    } );
} );