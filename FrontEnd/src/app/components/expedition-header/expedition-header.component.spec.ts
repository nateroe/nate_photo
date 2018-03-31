import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { APP_BASE_HREF } from '@angular/common';
import { AppRoutingModule } from '../../app-routing.module';
import { Expedition } from '../../model/expedition';
import { ExpeditionHeaderComponent } from './expedition-header.component';
import { SiteLinkComponent } from '../site-link/site-link.component';
import { BestOfGalleryComponent } from '../best-of-gallery/best-of-gallery.component';
import { PhotoDetailComponent } from '../photo-detail/photo-detail.component';
import { PhotoGalleryComponent } from '../photo-gallery/photo-gallery.component';
import { ExpeditionDetailComponent } from '../expedition-detail/expedition-detail.component';
import { ExpeditionGalleryComponent } from '../expedition-gallery/expedition-gallery.component';
import { ThumbnailComponent } from '../thumbnail/thumbnail.component';
import { ZoomViewComponent } from '../zoom-view/zoom-view.component';

describe( 'ExpeditionHeaderComponent', () => {
    let component: ExpeditionHeaderComponent;
    let fixture: ComponentFixture<ExpeditionHeaderComponent>;

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
        fixture = TestBed.createComponent( ExpeditionHeaderComponent );
        component = fixture.componentInstance;
    } );

    it( 'should create', () => {
        component.expedition = Object.assign( new Expedition(),
            {
                'id': 1,
                'title': 'Brice Creek',
                'description': 'A creek-side hike along Brice Creek in the Row River watershed.',
                'beginDate': new Date( 1506013145000 ),
                'endDate': new Date( 1506021068000 )
            } );
        fixture.detectChanges();
        expect( component ).toBeTruthy();
    } );
} );
