/**
 * NatePhoto - A photo catalog and presentation application.
 * Copyright (C) 2018 Nathaniel Roe
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * Contact nate [at] nateroe [dot] com
 */
import { TestBed, inject } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';

import { PhotoService } from './photo.service';
import { PHOTO, PHOTOS } from '../model/mock/mock-photo';
import { environment } from '../../environments/environment';

describe( 'PhotoService', () => {
    let httpClient: HttpClient;
    let httpTestingController: HttpTestingController;

    beforeEach(() => {
        TestBed.configureTestingModule( {
            providers: [PhotoService],
            imports: [HttpClientTestingModule]
        } );

        // Inject the http service and test controller for each test
        httpClient = TestBed.get( HttpClient );
        httpTestingController = TestBed.get( HttpTestingController );
    } );

    it( 'should be created', inject( [PhotoService], ( service: PhotoService ) => {
        expect( service ).toBeTruthy();
    } ) );

    it( 'should get photo by ID', inject( [PhotoService], ( service: PhotoService ) => {
        expect( service ).toBeTruthy();

        // Subscribe to the service under test
        service.getPhoto( PHOTO.id ).subscribe(
            data => {
                // Expect to get the test photo in response
                expect( data.id ).toEqual( PHOTO.id );
                expect( data.title ).toEqual( PHOTO.title );
            } );

        // expect a GET request for the photo
        const req = httpTestingController.expectOne( `/NatePhotoWebApp/rest/photo/id/${PHOTO.id}` );
        expect( req.request.method ).toEqual( 'GET' );

        // respond with the test photo
        req.flush( PHOTO );

        // verify GET was made
        httpTestingController.verify();
    } ) );
} );
