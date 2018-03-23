import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { APP_BASE_HREF, DecimalPipe } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subject } from 'rxjs/Subject';
import { RouterTestingModule } from '@angular/router/testing';

import { AppRoutingModule } from '../../app-routing.module';
import { SiteLinkComponent } from '../site-link/site-link.component';
import { ZoomViewComponent } from '../zoom-view/zoom-view.component';
import { PhotoDetailComponent } from './photo-detail.component';
import { PhotoService } from '../../services/photo.service';
import { GalleryContextService } from '../../services/gallery-context.service';
import { MockPhotoService } from '../../services/mock/mock-photo.service';

// XXX i stubbed this in but will it be useful?
class MockActiveRoute {
    params = new Subject<any>();
    queryParams = new Subject<any>();
}

describe( 'PhotoComponent', () => {
    let component: PhotoDetailComponent;
    let fixture: ComponentFixture<PhotoDetailComponent>;
    const mockActiveRoute: MockActiveRoute = new MockActiveRoute();

    beforeEach( async(() => {
        TestBed.configureTestingModule( {
            declarations: [
                PhotoDetailComponent,
                SiteLinkComponent,
                ZoomViewComponent
            ],
            imports: [
                RouterTestingModule,
                HttpClientModule
            ],
            providers: [
                { provide: APP_BASE_HREF, useValue: '/' },
                DecimalPipe,
                GalleryContextService,
                { provide: ActivatedRoute, useClass: MockActiveRoute },
            ]
        } )
            .overrideComponent( PhotoDetailComponent, {
                set: {
                    providers: [
                        { provide: PhotoService, useClass: MockPhotoService },
                    ]
                }
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
