import { TestBed, async } from '@angular/core/testing';
import { APP_BASE_HREF } from '@angular/common';
import { AppComponent } from './app.component';
import { BestOfGalleryComponent } from './components/best-of-gallery/best-of-gallery.component';
import { NavigationBarComponent } from './components/navigation-bar/navigation-bar.component';
import { PhotoDetailComponent } from './components/photo-detail/photo-detail.component';
import { PhotoGalleryComponent } from './components/photo-gallery/photo-gallery.component';
import { AppRoutingModule } from './/app-routing.module';
import { ExpeditionGalleryComponent } from './components/expedition-gallery/expedition-gallery.component';
import { ExpeditionDetailComponent } from './components/expedition-detail/expedition-detail.component';
import { ExpeditionHeaderComponent } from './components/expedition-header/expedition-header.component';
import { SiteLinkComponent } from './components/site-link/site-link.component';
import { ThumbnailComponent } from './components/thumbnail/thumbnail.component';
import { ZoomViewComponent } from './components/zoom-view/zoom-view.component';

describe( 'AppComponent', () => {
    beforeEach( async(() => {
        TestBed.configureTestingModule( {
            declarations: [
                AppComponent,
                BestOfGalleryComponent,
                ExpeditionDetailComponent,
                ExpeditionGalleryComponent,
                ExpeditionHeaderComponent,
                NavigationBarComponent,
                PhotoGalleryComponent,
                PhotoDetailComponent,
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
        } ).compileComponents();
    } ) );

    it( 'should create the app', async(() => {
        const fixture = TestBed.createComponent( AppComponent );
        const app = fixture.debugElement.componentInstance;
        expect( app ).toBeTruthy();
    } ) );

    it( `should have as title 'NatePhoto'`, async(() => {
        const fixture = TestBed.createComponent( AppComponent );
        const app = fixture.debugElement.componentInstance;
        expect( app.title ).toEqual( 'NatePhoto' );
    } ) );

    // XXX replace with something useful
    //    it( 'should render title in a h1 tag', async(() => {
    //        const fixture = TestBed.createComponent( AppComponent );
    //        fixture.detectChanges();
    //        const compiled = fixture.debugElement.nativeElement;
    //        expect( compiled.querySelector( 'h1' ).textContent ).toContain( 'Welcome to app!' );
    //    } ) );
} );
