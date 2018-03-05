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
        //        const url = `http://192.168.1.50:8080/NatePhotoWebApp/rest/photo/id/${id}`;
        const url = `/NatePhotoWebApp/rest/photo/id/${id}`;
        console.log( "PhotoServce.getPhoto(" + id + ")" );
        return this.http.get<Photo>( url ).pipe( tap( data => console.log( "PhotoService.getPhoto(...) results: " + data ) ) );
    }

    getPhotos(): Observable<Photo[]> {
        // XXX this URL is just temporary for dev. 
        //        const url = `http://192.168.1.50:8080/NatePhotoWebApp/rest/photo/all`;
        const url = `/NatePhotoWebApp/rest/photo/all`;
        console.log( "PhotoServce.getPhotos()" );
        return this.http.get<Photo[]>( url ).pipe( tap( data => console.log( "PhotoService.getPhotos() results: " + data ) ) );
    }

    /**
     * Assuming that images is ordered by decreasing size, choose the largest image that fits in the requested area.
     * 
     * @param width
     * @param height
     */
    getBestResource( resources: ImageResource[], width: number, height: number ): ImageResource {
        console.log( "---- getBestResource(...)" );
        let result: ImageResource = null;
        for ( let image of resources ) {
            result = image;
            console.log( "Examine image resource " + result.url + " (req area: " + ( width * height ) + " >= image area: " + ( image.width * image.height ) + ")" );
            // find first image smaller than or equal to the requested area
            if ( width * height >= image.width * image.height ) {
                console.log( "Make a selection!" );
                break;
            }
        }

        // XXX uncomment to point at 192.168.1.50 when desired 
        //        result.url = 'http://192.168.1.50:8080' + result.url;

        console.log( "Returning best image resource " + result.url + " (width: " + width + " >= " + result.width );
        return result;
    }
}