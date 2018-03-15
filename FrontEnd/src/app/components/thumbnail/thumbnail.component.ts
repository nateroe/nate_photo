import { ChangeDetectorRef, Component, ElementRef, Input, OnInit, ViewChild } from '@angular/core';
import { RenderedPhoto } from '../../model/rendered-photo';
import { ImageResource } from '../../model/image-resource';

@Component( {
    selector: 'app-thumbnail',
    templateUrl: './thumbnail.component.html',
    styleUrls: ['./thumbnail.component.css']
} )
export class ThumbnailComponent implements OnInit {
    @ViewChild( 'photoChild' ) photoElement: ElementRef;

    @Input()
    photo: RenderedPhoto;

    isMouseOver: boolean = false;

    constructor( public changeDetectorRef: ChangeDetectorRef ) { }

    ngOnInit() {
    }

    mouseEnter(): void {
        console.log( "thumbnail mouseEnter" );

        this.isMouseOver = true;
    }

    mouseLeave(): void {
        this.isMouseOver = false;
    }

    /**
     * Flip this photo's isLoaded flag based on whether the photo has ever been on screen
     */
    doDelayedLoad(): void {
        if ( this.photoElement && this.photo ) {
            let top: number = this.photoElement.nativeElement.getBoundingClientRect().top
            let bottom: number = this.photoElement.nativeElement.getBoundingClientRect().bottom

            // The element is on screen if either the top of the image is within the window,
            // or the bottom of the image is within the window,
            // or the top of the image is above the window and the bottom of the image is below.
            let isOnScreen: boolean = ( top >= 0 && top < window.innerHeight )
                || ( bottom >= 0 && bottom < window.innerHeight )
                || ( top < 0 && bottom >= window.innerHeight );

            this.photo.isLoaded = this.photo.isLoaded || isOnScreen;
        } else {
            console.log( "No photo for " + this.photo.id );
        }

        // reflect changes back to the view
        this.changeDetectorRef.detectChanges()
    }
}
