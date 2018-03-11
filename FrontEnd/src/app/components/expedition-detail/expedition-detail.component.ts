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
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, ParamMap } from '@angular/router';
import 'rxjs/add/operator/switchMap';
import { ExpeditionService } from '../../services/expedition.service';
import { PhotoService } from '../../services/photo.service';
import { Expedition } from '../../model/expedition';
import { RenderedPhoto } from '../../model/rendered-photo';
import { PhotoGalleryComponent } from '../photo-gallery/photo-gallery.component';

@Component( {
    selector: 'app-expedition-detail',
    templateUrl: './expedition-detail.component.html',
    styleUrls: ['./expedition-detail.component.css']
} )
export class ExpeditionDetailComponent implements OnInit {
    expedition: Expedition;
    photos: RenderedPhoto[];

    constructor( private route: ActivatedRoute, private expeditionService: ExpeditionService, private photoService: PhotoService ) { }

    ngOnInit() {
        this.route.paramMap
            .switchMap(( params: ParamMap ) => {
                let expeditionId: number = parseInt( params.get( 'expeditionId' ), 10 );
                return this.expeditionService.getExpedition( expeditionId );
            } )
            .subscribe(
            expedition => {
                this.expedition = expedition;
                this.photoService
                    //.getAllPhotos()
                    .getPhotosByExpedition( expedition.id )
                    .subscribe(
                    data => {
                        console.log( "ExpeditionDetailComponent.sortPhotos() ---->" );
                        this.photos = data;
                        this.sortPhotos();
                        console.log( "ExpeditionDetailComponent.sortPhotos() <----" );
                    } );
            } );
    }

    /**
     * Sort photos by date, ascending (chronologically)
     */
    sortPhotos(): void {
        this.photos = this.photos.sort(( a: RenderedPhoto, b: RenderedPhoto ) => {
            return a.date.getTime() - b.date.getTime();
        } );
    }
}
