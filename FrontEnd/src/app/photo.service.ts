import { Injectable } from '@angular/core';
import { Location } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { of } from 'rxjs/observable/of';
import { map } from 'rxjs/operators/map';
import 'rxjs/add/observable/from';
import 'rxjs/add/operator/map';
import { switchMap } from 'rxjs/operators/switchmap';
import { tap } from 'rxjs/operators/tap';
import { catchError } from 'rxjs/operators/catchError';

import { environment } from '../environments/environment';
import { Photo } from './photo';
import { RenderedPhoto } from './rendered-photo';
import { ImageResource } from './image-resource';
import { PHOTO } from './mock-photo';

/**
 * Service to request Photo data
 */
@Injectable()
export class PhotoService {
    constructor( private http: HttpClient ) {
    }

    getPhoto( photoId: number ): Observable<RenderedPhoto> {
        const url = environment.restBaseUrl + `/NatePhotoWebApp/rest/photo/id/${photoId}`;
        console.log( "PhotoServce.getPhoto(" + photoId + ")" );
        return this.http.get<RenderedPhoto>( url ).pipe(
            map(( jsonPhoto: RenderedPhoto ) => {
                return new RenderedPhoto().copyFrom( jsonPhoto );
            } ),
            tap( data => console.log( "PhotoService.getPhoto(...) results: " + data ) ) );

    }

    getPhotosByExpedition( expeditionId: number ): Observable<RenderedPhoto[]> {
        const url = environment.restBaseUrl + `/NatePhotoWebApp/rest/expedition/${expeditionId}`;
        console.log( "PhotoServce.getPhotosByExpedition(" + expeditionId + ")" );
        return this.http.get<RenderedPhoto[]>( url ).pipe(
            map(( data: Photo[] ) => {
                let result: RenderedPhoto[] = new Array();

                for ( let photo of data ) {
                    result.push( new RenderedPhoto().copyFrom( photo ) );
                }

                return result;
            } ),
            tap( data => console.log( "PhotoService.getPhotosByExpedition(...) results: " + data ) ) );
    }

    getPhotos(): Observable<RenderedPhoto[]> {
        const url = environment.restBaseUrl + `/NatePhotoWebApp/rest/photo/all`;
        console.log( "PhotoServce.getPhotos()" );
        return this.http.get<Photo[]>( url ).pipe( map(( data: Photo[] ) => {
            let result: RenderedPhoto[] = new Array();

            for ( let photo of data ) {
                result.push( new RenderedPhoto().copyFrom( photo ) );
            }

            return result;
        } ), tap( data => console.log( "PhotoService.getPhotos() results: " + data ) ) );
    }
}
