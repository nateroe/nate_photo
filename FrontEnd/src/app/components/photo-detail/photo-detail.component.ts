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
import { Component, ElementRef, HostListener, Input, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, ParamMap } from '@angular/router';
import 'rxjs/add/operator/switchMap';
import { RenderedPhoto } from '../../model/rendered-photo';
import { PhotoService } from '../../services/photo.service';
import { ImageResource } from '../../model/image-resource';

/**
 * Component for a single Photo
 */
@Component( {
    selector: 'app-photo',
    templateUrl: './photo-detail.component.html',
    styleUrls: ['./photo-detail.component.css']
} )
export class PhotoDetailComponent implements OnInit {
    @ViewChild( 'wrapper' ) wrapper: ElementRef;

    photo: RenderedPhoto;
    bestResourceUrl: string;
    isZoomVisible: boolean = false;

    lastClickX: number;
    lastClickY: number;

    @HostListener( 'window:resize', ['$event'] ) windowResize( event: any ) {
        this.chooseBestResource();
    }

    constructor( private route: ActivatedRoute, private photoService: PhotoService ) {
    }

    /**
     * Parse the ID from the route, and use the PhotoService to request the given Photo
     */
    ngOnInit(): void {
        this.route.paramMap
            .switchMap(( params: ParamMap ) => {
                const photoId: number = parseInt( params.get( 'photoId' ), 10 );
                return this.photoService.getPhoto( photoId );
            } )
            .subscribe(
            data => {
                this.photo = data;
                this.chooseBestResource();
            } );
    }

    /**
     * Choose the best resource given the size of our window
     */
    chooseBestResource(): void {
        if ( this.photo ) {
            const ratio: number = this.photo.images[0].width / this.photo.images[0].height;
            this.photo.height = ( document.documentElement.clientHeight - 50 ) * 0.8;
            this.photo.width = this.photo.height * ratio;

            if ( this.wrapper && this.photo.width > document.documentElement.clientWidth ) {
                this.photo.width = this.wrapper.nativeElement.clientWidth;
                this.photo.height = this.photo.width / ratio;
            }

            this.bestResourceUrl = this.photo.getBestResourceUrl();

            console.log( 'chooseBestResource: (' + this.photo.width + 'x'
                + this.photo.height + ' ratio ' + ratio + ') : ' + this.bestResourceUrl );
        }
    }

    /**
     * Toggle the zoom view
     */
    zoomView( event?: MouseEvent ): void {
        if ( event ) {
            // keep track of the point where the user
            // initially clicked. we'll use it later to set
            // the initial zoom view panning position
            this.lastClickX = event.clientX;
            this.lastClickY = event.clientY;
        }

        this.isZoomVisible = !this.isZoomVisible;
    }
}
