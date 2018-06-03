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
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable ,  of } from 'rxjs';
import { map ,  tap ,  catchError } from 'rxjs/operators';

import { environment } from '../../environments/environment';
import { Expedition } from '../model/expedition';

@Injectable()
export class ExpeditionService {
    constructor( private http: HttpClient ) {
    }

    getExpedition( id: number ): Observable<Expedition> {
        const url = environment.restBaseUrl + `/NatePhotoWebApp/rest/expedition/id/${id}`;
        console.log( 'ExpeditionService.getExpedition(' + id + ')' );
        return this.http.get<Expedition>( url ).pipe(
            map(( jsonExpedition: Expedition ) => {
                return new Expedition().copyFrom( jsonExpedition );
            } ),
            tap( data => console.log( 'ExpeditionService.getExpedition(...) results: ' + data ) ) );
    }

    getAllExpeditions(): Observable<Expedition[]> {
        const url = environment.restBaseUrl + `/NatePhotoWebApp/rest/expedition/all`;
        console.log( 'ExpeditionService.getAllExpedtions()' );
        return this.http.get<Expedition[]>( url ).pipe(
            map(( data: Expedition[] ) => {
                const result: Expedition[] = new Array();

                for ( const expedition of data ) {
                    result.push( new Expedition().copyFrom( expedition ) );
                }

                return result;
            } ),
            tap( data => console.log( 'ExpeditionService.getAllExpedtions() results: ' + data ) ) );
    }
}
