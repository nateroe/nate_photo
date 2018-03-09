import { Component, ElementRef, HostListener, Input, OnInit, ViewChild } from '@angular/core';
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

    @HostListener( 'window:resize', ['$event'] ) windowResize( event: any ) {
        this.chooseBestResource();
    }

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
            data => {
                this.photo = data;
                this.chooseBestResource();
            } );
    }

    chooseBestResource() {
        //        let width: number = this.wrapper.nativeElement.clientWidth;
        //        this.bestResource = this.photo.getBestResourceByArea( width * width * 0.666 );
        this.bestResource = this.photo.getBestResourceByArea( 1024 * 1024 * 0.666 );
    }
}
