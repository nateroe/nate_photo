import { Component, OnInit } from '@angular/core';
import { Photo } from '../photo';
import { ImageResource } from '../image-resource';
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
            jsonPhotos => {
                // clear this.photos
                this.photos = new Array();
                for ( let jsonPhoto of jsonPhotos ) {
                    this.photos.push( new Photo().copyFrom( jsonPhoto ) );
                }
            } );
    }

    onSelect( photo: Photo ): void {
    }
}
