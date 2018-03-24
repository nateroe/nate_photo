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
import { Injectable } from '@angular/core';
import { Location } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/observable/of';
import { map } from 'rxjs/operators/map';
import { tap } from 'rxjs/operators/tap';
import { catchError } from 'rxjs/operators/catchError';

import { environment } from '../../../environments/environment';
import { Photo } from '../../model/photo';
import { RenderedPhoto } from '../../model/rendered-photo';
import { ImageResource } from '../../model/image-resource';
import { RENDERED_PHOTO, RENDERED_PHOTOS } from '../../model/mock/mock-rendered-photo';

/**
 * Mock PhotoService returns mock data
 */
@Injectable()
export class MockPhotoService {
    constructor() {
    }

    getPhoto( photoId: number ): Observable<RenderedPhoto> {
        return Observable.of( RENDERED_PHOTO );
    }

    getPhotosByExpedition( expeditionId: number ): Observable<RenderedPhoto[]> {
        return Observable.of( RENDERED_PHOTOS );
    }

    getPhotoHighlightsByExpedition( expeditionId: number ): Observable<RenderedPhoto[]> {
        return Observable.of( RENDERED_PHOTOS );
    }

    getBestPhotos(): Observable<RenderedPhoto[]> {
        return Observable.of( RENDERED_PHOTOS );
    }

    getAllPhotos(): Observable<RenderedPhoto[]> {
        return Observable.of( RENDERED_PHOTOS );
    }
}
