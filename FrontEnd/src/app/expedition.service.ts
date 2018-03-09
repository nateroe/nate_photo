import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { of } from 'rxjs/observable/of';
import { map } from 'rxjs/operators/map';
import { tap } from 'rxjs/operators/tap';
import { catchError } from 'rxjs/operators/catchError';

import { environment } from '../environments/environment';
import { Expedition } from './expedition';

@Injectable()
export class ExpeditionService {
    constructor( private http: HttpClient ) {
    }

    getExpedition( id: number ): Observable<Expedition> {
        const url = environment.restBaseUrl + `/NatePhotoWebApp/rest/expedition/id/${id}`;
        console.log( "ExpeditionService.getExpedition(" + id + ")" );
        return this.http.get<Expedition>( url ).pipe(
            map(( jsonExpedition: Expedition ) => {
                return new Expedition().copyFrom( jsonExpedition );
            } ),
            tap( data => console.log( "ExpeditionService.getExpedition(...) results: " + data ) ) );
    }

    getAllExpeditions(): Observable<Expedition[]> {
        const url = environment.restBaseUrl + `/NatePhotoWebApp/rest/expedition/all`;
        console.log( "ExpeditionService.getAllExpedtions()" );
        return this.http.get<Expedition[]>( url ).pipe(
            map(( data: Expedition[] ) => {
                let result: Expedition[] = new Array();

                for ( let expedition of data ) {
                    result.push( new Expedition().copyFrom( expedition ) );
                }

                return result;
            } ),
            tap( data => console.log( "ExpeditionService.getAllExpedtions() results: " + data ) ) );
    }
}
