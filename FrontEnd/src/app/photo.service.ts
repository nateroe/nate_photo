import { Injectable } from '@angular/core';
import { Location } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { of } from 'rxjs/observable/of';
import { tap } from 'rxjs/operators/tap';
import { catchError } from 'rxjs/operators/catchError';

import { environment } from '../environments/environment';
import { Photo } from './photo';
import { ImageResource } from './image-resource';
import { PHOTO } from './mock-photo';

/**
 * Service to request Photo data
 */
@Injectable()
export class PhotoService {
    constructor( private http: HttpClient ) {
    }

    getPhoto( id: number ): Observable<Photo> {
        const url = environment.restBaseUrl + `/NatePhotoWebApp/rest/photo/id/${id}`;
        console.log( "PhotoServce.getPhoto(" + id + ")" );
        return this.http.get<Photo>( url ).pipe( tap( data => console.log( "PhotoService.getPhoto(...) results: " + data ) ) );
    }

    getPhotos(): Observable<Photo[]> {
        const url = environment.restBaseUrl + `/NatePhotoWebApp/rest/photo/all`;
        console.log( "PhotoServce.getPhotos()" );
        return this.http.get<Photo[]>( url ).pipe( tap( data => console.log( "PhotoService.getPhotos() results: " + data ) ) );
    }
}
