import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientModule } from '@angular/common/http';

import { PhotoGalleryComponent } from './photo-gallery.component';
import { ThumbnailComponent } from '../thumbnail/thumbnail.component';
import { PhotoService } from '../../services/photo.service';
import { MockPhotoService } from '../../services/mock/mock-photo.service';
import { GalleryContextService } from '../../services/gallery-context.service';
import { RENDERED_PHOTO, RENDERED_PHOTOS } from '../../model/mock/mock-rendered-photo';

describe( 'PhotoGalleryComponent', () => {
    let component: PhotoGalleryComponent;
    let fixture: ComponentFixture<PhotoGalleryComponent>;

    beforeEach( async(() => {
        TestBed.configureTestingModule( {
            declarations: [
                PhotoGalleryComponent,
                ThumbnailComponent
            ],
            imports: [
                RouterTestingModule,
                HttpClientModule
            ],
            providers: [
                GalleryContextService
            ]
        } )
            .overrideComponent( PhotoGalleryComponent, {
                set: {
                    providers: [
                        { provide: PhotoService, useClass: MockPhotoService },
                    ]
                }
            } )
            .compileComponents();
    } ) );

    beforeEach(() => {
        fixture = TestBed.createComponent( PhotoGalleryComponent );
        component = fixture.componentInstance;
    } );

    it( 'should create', () => {
        component.photos = RENDERED_PHOTOS;
        fixture.detectChanges();
        expect( component ).toBeTruthy();
    } );
} );
