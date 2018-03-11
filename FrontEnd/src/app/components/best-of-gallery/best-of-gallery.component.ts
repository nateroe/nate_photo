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
