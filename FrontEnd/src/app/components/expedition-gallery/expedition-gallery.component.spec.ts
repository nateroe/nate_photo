import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientModule } from '@angular/common/http';
import { RouterTestingModule } from '@angular/router/testing';

import { ExpeditionGalleryComponent } from './expedition-gallery.component';
import { ExpeditionHeaderComponent } from '../expedition-header/expedition-header.component';
import { PhotoGalleryComponent } from '../photo-gallery/photo-gallery.component';
import { ThumbnailComponent } from '../thumbnail/thumbnail.component';
import { PhotoService } from '../../services/photo.service';
import { MockPhotoService } from '../../services/mock/mock-photo.service';
import { ExpeditionService } from '../../services/expedition.service';
import { MockExpeditionService } from '../../services/mock/mock-expedition.service';
import { GalleryContextService } from '../../services/gallery-context.service';
import { SiteLinkComponent } from '../site-link/site-link.component';



describe( 'ExpeditionGalleryComponent', () => {
    let component: ExpeditionGalleryComponent;
    let fixture: ComponentFixture<ExpeditionGalleryComponent>;

    beforeEach( async(() => {
        TestBed.configureTestingModule( {
            declarations: [ExpeditionGalleryComponent,
                ExpeditionHeaderComponent,
                PhotoGalleryComponent,
                SiteLinkComponent,
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
            .overrideComponent( ExpeditionGalleryComponent, {
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
        fixture = TestBed.createComponent( ExpeditionGalleryComponent );
        component = fixture.componentInstance;
        fixture.detectChanges();
    } );

    it( 'should create', () => {
        expect( component ).toBeTruthy();
    } );
} );
