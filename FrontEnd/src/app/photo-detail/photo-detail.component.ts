import { Component, OnInit, Input, ElementRef, ViewChild } from '@angular/core';
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
    @ViewChild( 'wrapper' ) wrapper: ElementRef

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
                this.photo = jsonPhoto;
                let width: number = this.wrapper.nativeElement.clientWidth;
                this.bestResource = this.photo.getBestResourceByArea( width * width * 0.666 );
            } );
    }
}
