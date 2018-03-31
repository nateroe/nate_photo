import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';

import { ThumbnailComponent } from './thumbnail.component';
import { GalleryContextService } from '../../services/gallery-context.service';
import { RENDERED_PHOTO, RENDERED_PHOTOS } from '../../model/mock/mock-rendered-photo';

describe( 'ThumbnailComponent', () => {
    let component: ThumbnailComponent;
    let fixture: ComponentFixture<ThumbnailComponent>;

    beforeEach( async(() => {
        TestBed.configureTestingModule( {
            declarations: [ThumbnailComponent],
            imports: [RouterTestingModule.withRoutes( [] )],
            providers: [GalleryContextService]
        } )
            .compileComponents();
    } ) );

    beforeEach(() => {
        fixture = TestBed.createComponent( ThumbnailComponent );
        component = fixture.componentInstance;
    } );

    it( 'should create', () => {
        component.photo = RENDERED_PHOTO;
        fixture.detectChanges();
        expect( component ).toBeTruthy();
    } );
} );
