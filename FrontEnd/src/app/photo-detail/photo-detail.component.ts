import { Component, OnInit, Input } from '@angular/core';
import { ActivatedRoute, ParamMap } from '@angular/router';
import 'rxjs/add/operator/switchMap';
import { Photo } from '../photo';
import { PhotoService } from '../photo.service';
import { ImageResource } from '../image-resource';

/*
 * Component for a single Photo
 */
@Component( {
    selector: 'app-photo',
    templateUrl: './photo-detail.component.html',
    styleUrls: ['./photo-detail.component.css']
} )
export class PhotoDetailComponent implements OnInit {
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
            jsonPhoto => {
                this.photo = new Photo().copyFrom( jsonPhoto );
                this.bestResource = this.photo.getBestResourceByArea( this.width * this.width );
            } );
    }
}
