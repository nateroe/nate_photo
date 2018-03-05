import { Component, OnInit, Input } from '@angular/core';
import { ActivatedRoute, ParamMap } from '@angular/router';
import 'rxjs/add/operator/switchMap';
import { Photo } from '../photo';
import { PhotoService } from '../photo.service';

/*
 * Component for a single Photo
 */
@Component( {
    selector: 'app-photo',
    templateUrl: './photo.component.html',
    styleUrls: ['./photo.component.css']
} )
export class PhotoComponent implements OnInit {
    width: number = 1024;
    photo: Photo;
    bestResource: ImageResource;

    constructor( private route: ActivatedRoute, private photoService: PhotoService ) {
    }

    /**
     * Parse the ID from the route, and use the PhotoService to request the given Photo
     */
    ngOnInit() {
        this.route.paramMap
            .switchMap(( params: ParamMap ) => {
                let photoId: number = parseInt( params.get( 'photoId' ), 10 );
                return this.photoService.getPhoto( photoId );
            } )
            .subscribe(
            photo => {
                this.photo = photo;
                this.bestResource = this.photoService.getBestResource( this.photo.images, this.width, this.width );
            } );
    }

    getFlashString(): string {
        return ( ( this.photo.flash & 0x01 ) != 0 ) ? "Yes" : "No";
    }
}
