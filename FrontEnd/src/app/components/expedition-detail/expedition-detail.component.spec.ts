import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';

import { ExpeditionDetailComponent } from './expedition-detail.component';
import { ExpeditionHeaderComponent } from '../expedition-header/expedition-header.component';
import { PhotoGalleryComponent } from '../photo-gallery/photo-gallery.component';
import { ThumbnailComponent } from '../thumbnail/thumbnail.component';
import { PhotoService } from '../../services/photo.service';
import { MockPhotoService } from '../../services/mock/mock-photo.service';
import { ExpeditionService } from '../../services/expedition.service';
import { MockExpeditionService } from '../../services/mock/mock-expedition.service';
import { GalleryContextService } from '../../services/gallery-context.service';
import { SiteLinkComponent } from '../site-link/site-link.component';

describe( 'ExpeditionDetailComponent', () => {
    let component: ExpeditionDetailComponent;
    let fixture: ComponentFixture<ExpeditionDetailComponent>;

    beforeEach( async(() => {
        TestBed.configureTestingModule( {
            declarations: [
                ExpeditionDetailComponent,
                ExpeditionHeaderComponent,
                PhotoGalleryComponent,
                SiteLinkComponent,
                ThumbnailComponent],
            imports: [
                RouterTestingModule,
            ],
            providers: [
                GalleryContextService
            ]
        } )
            .overrideComponent( ExpeditionDetailComponent, {
                set: {
                    providers: [
                        { provide: ExpeditionService, useClass: MockExpeditionService },
                        { provide: PhotoService, useClass: MockPhotoService },
                    ]
                }
            } )
            .compileComponents();
    } ) );

    beforeEach(() => {
        fixture = TestBed.createComponent( ExpeditionDetailComponent );
        component = fixture.componentInstance;
        fixture.detectChanges();
    } );

    it( 'should create', () => {
        expect( component ).toBeTruthy();
    } );
} );
