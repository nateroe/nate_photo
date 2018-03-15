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

import { PhotoService } from '../../services/photo.service';
import { RenderedPhoto } from '../../model/rendered-photo';

/**
 * The BestOfGallery presents a selection of the best photos, by EXIF rating.
 * The back end returns the best 25 photos that are rated at least 4.
 * This component then shuffles the results.
 */
@Component( {
    selector: 'app-best-of-gallery',
    templateUrl: './best-of-gallery.component.html',
    styleUrls: ['./best-of-gallery.component.css']
} )
export class BestOfGalleryComponent implements OnInit {
    photos: RenderedPhoto[];

    constructor( private photoService: PhotoService ) { }

    ngOnInit() {
        this.photoService.getBestPhotos().subscribe(
            data => {
                this.photos = data;
                this.shufflePhotos();
            } );
    }

    /**
     * Knuth/Fisher-Yates shuffle
     */
    shufflePhotos(): void {
        let end = this.photos.length - 1;
        for ( let i: number = 0; i < end; i++ ) {
            let rand: number = Math.floor( Math.random() * ( end - i ) ) + i;
            let tmp: RenderedPhoto = this.photos[i];
            this.photos[i] = this.photos[rand];
            this.photos[rand] = tmp;
        }
    }
}
