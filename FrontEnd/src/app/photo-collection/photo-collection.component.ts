import { Component, OnInit } from '@angular/core';
import { Photo } from '../photo';
import { PhotoService } from '../photo.service';

@Component( {
    selector: 'app-photo-collection',
    templateUrl: './photo-collection.component.html',
    styleUrls: ['./photo-collection.component.css']
} )
export class PhotoCollectionComponent implements OnInit {
    photos: Photo[];
    bestImages: ImageResource[];

    constructor( private photoService: PhotoService ) {

    }

    ngOnInit() {
        this.photoService.getPhotos().subscribe(
            photos => {
                this.photos = photos;
                // and ... do something?
            } );
    }

    onSelect( photo: Photo ): void {
    }
}
