import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { APP_BASE_HREF } from '@angular/common';
import { AppRoutingModule } from '../../app-routing.module';
import { SiteLinkComponent } from './site-link.component';
import { BestOfGalleryComponent } from '../best-of-gallery/best-of-gallery.component';
import { PhotoDetailComponent } from '../photo-detail/photo-detail.component';
import { PhotoGalleryComponent } from '../photo-gallery/photo-gallery.component';
import { ExpeditionDetailComponent } from '../expedition-detail/expedition-detail.component';
import { ExpeditionGalleryComponent } from '../expedition-gallery/expedition-gallery.component';
import { ExpeditionHeaderComponent } from '../expedition-header/expedition-header.component';
import { ThumbnailComponent } from '../thumbnail/thumbnail.component';
import { ZoomViewComponent } from '../zoom-view/zoom-view.component';

describe( 'SiteLinkComponent', () => {
    let component: SiteLinkComponent;
    let fixture: ComponentFixture<SiteLinkComponent>;

    beforeEach( async(() => {
        TestBed.configureTestingModule( {
            declarations: [
                BestOfGalleryComponent,
                ExpeditionDetailComponent,
                ExpeditionGalleryComponent,
                ExpeditionHeaderComponent,
                PhotoDetailComponent,
                PhotoGalleryComponent,
                SiteLinkComponent,
                ThumbnailComponent,
                ZoomViewComponent
            ],
            imports: [
                AppRoutingModule
            ],
            providers: [
                { provide: APP_BASE_HREF, useValue: '/' }
            ]
        } )
            .compileComponents();
    } ) );

    beforeEach(() => {
        fixture = TestBed.createComponent( SiteLinkComponent );
        component = fixture.componentInstance;
    } );

    it( 'should create', () => {
        component.linkText = 'Multi-word link text';
        component.linkDestination = 'testDestination';
        component.isImageFirst = true;
        component.svgId = 'foo';
        fixture.detectChanges();
        expect( component ).toBeTruthy();
    } );
} );
