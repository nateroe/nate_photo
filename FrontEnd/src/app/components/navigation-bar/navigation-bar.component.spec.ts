import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { AppRoutingModule } from '../../app-routing.module';
import { APP_BASE_HREF } from '@angular/common';
import { NavigationBarComponent } from './navigation-bar.component';
import { SiteLinkComponent } from '../site-link/site-link.component';
import { BestOfGalleryComponent } from '../best-of-gallery/best-of-gallery.component';
import { PhotoDetailComponent } from '../photo-detail/photo-detail.component';
import { PhotoGalleryComponent } from '../photo-gallery/photo-gallery.component';
import { ExpeditionDetailComponent } from '../expedition-detail/expedition-detail.component';
import { ExpeditionGalleryComponent } from '../expedition-gallery/expedition-gallery.component';
import { ExpeditionHeaderComponent } from '../expedition-header/expedition-header.component';
import { ThumbnailComponent } from '../thumbnail/thumbnail.component';
import { ZoomViewComponent } from '../zoom-view/zoom-view.component';

describe( 'NavigationBarComponent', () => {
    let component: NavigationBarComponent;
    let fixture: ComponentFixture<NavigationBarComponent>;

    beforeEach( async(() => {
        TestBed.configureTestingModule( {
            declarations: [
                BestOfGalleryComponent,
                ExpeditionDetailComponent,
                ExpeditionGalleryComponent,
                ExpeditionHeaderComponent,
                NavigationBarComponent,
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
        fixture = TestBed.createComponent( NavigationBarComponent );
        component = fixture.componentInstance;
        fixture.detectChanges();
    } );

    it( 'should create', () => {
        expect( component ).toBeTruthy();
    } );
} );
