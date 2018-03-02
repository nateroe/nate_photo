import { Injectable } from '@angular/core';
import { Location } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { of } from 'rxjs/observable/of';
import { tap } from 'rxjs/operators/tap';
import { catchError } from 'rxjs/operators/catchError';
import { Photo } from './photo';
import { PHOTO } from './mock_photo';

/**
 * Service to request Photo data
 */
@Injectable()
export class PhotoService {
    constructor( private http: HttpClient ) {
    }

    getPhoto( id: number ): Observable<Photo> {
        // XXX this URL is just temporary for dev. 
        const url = `http://192.168.1.50:8080/NatePhotoWebApp/rest/photo/${id}`;
        console.log( "PhotoServce.getPhoto(" + id + ")" );
        return this.http.get<Photo>( url ).pipe( tap( data => console.log( "PhotoService.getPhoto(...) results: " + data ) ) );
    }
}
