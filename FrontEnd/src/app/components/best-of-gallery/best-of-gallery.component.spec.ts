import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientModule } from '@angular/common/http';
import { RouterTestingModule } from '@angular/router/testing';

import { BestOfGalleryComponent } from './best-of-gallery.component';
import { PhotoGalleryComponent } from '../photo-gallery/photo-gallery.component';
import { ThumbnailComponent } from '../thumbnail/thumbnail.component';
import { PhotoService } from '../../services/photo.service';
import { MockPhotoService } from '../../services/mock/mock-photo.service';
import { GalleryContextService } from '../../services/gallery-context.service';

describe( 'BestOfGalleryComponent', () => {
    let component: BestOfGalleryComponent;
    let fixture: ComponentFixture<BestOfGalleryComponent>;

    beforeEach( async(() => {
        TestBed.configureTestingModule( {
            declarations: [
                BestOfGalleryComponent,
                PhotoGalleryComponent,
                ThumbnailComponent],
            imports: [
                RouterTestingModule, HttpClientModule
            ],
            providers: [
                GalleryContextService
            ]
        } )
            .overrideComponent( BestOfGalleryComponent, {
                set: {
                    providers: [
                        { provide: PhotoService, useClass: MockPhotoService },
                    ]
                }
            } )
            .compileComponents();
    } ) );

    beforeEach(() => {
        fixture = TestBed.createComponent( BestOfGalleryComponent );
        component = fixture.componentInstance;
    } );

    it( 'should create', () => {
        fixture.detectChanges();
        expect( component ).toBeTruthy();
    } );
} );
