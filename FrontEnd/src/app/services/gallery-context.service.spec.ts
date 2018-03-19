import { TestBed, inject } from '@angular/core/testing';

import { GalleryContextService } from './gallery-context.service';

describe( 'RecentGalleryServiceService', () => {
    beforeEach(() => {
        TestBed.configureTestingModule( {
            providers: [GalleryContextService]
        } );
    } );

    it( 'should be created', inject( [GalleryContextService], ( service: GalleryContextService ) => {
        expect( service ).toBeTruthy();
    } ) );
} );
