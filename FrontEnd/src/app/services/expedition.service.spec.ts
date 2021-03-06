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

import { ExpeditionService } from './expedition.service';
import { EXPEDITION, EXPEDITIONS } from '../model/mock/mock-expedition';

describe( 'ExpeditionService', () => {
    let httpClient: HttpClient;
    let httpTestingController: HttpTestingController;

    beforeEach(() => {
        TestBed.configureTestingModule( {
            providers: [ExpeditionService],
            imports: [HttpClientTestingModule]
        } );

        // Inject the http service and test controller for each test
        httpClient = TestBed.get( HttpClient );
        httpTestingController = TestBed.get( HttpTestingController );
    } );

    it( 'should be created', inject( [ExpeditionService], ( service: ExpeditionService ) => {
        expect( service ).toBeTruthy();
    } ) );
} );
