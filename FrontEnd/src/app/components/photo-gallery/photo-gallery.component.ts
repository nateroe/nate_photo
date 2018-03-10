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
import { ChangeDetectorRef, Component, ElementRef, HostListener, Input, OnChanges, Output, ViewChild } from '@angular/core';
import { RenderedPhoto } from '../../model/rendered-photo';
import { ImageResource } from '../../model/image-resource';
import { PhotoService } from '../../services/photo.service';

@Component( {
    selector: 'app-photo-gallery',
    templateUrl: './photo-gallery.component.html',
    styleUrls: ['./photo-gallery.component.css']
} )
export class PhotoGalleryComponent implements OnChanges {
    @ViewChild( 'wrapper' ) wrapper: ElementRef

    @HostListener( 'window:resize', ['$event'] ) windowResize( event: any ) {
        this.layout(); // includes resize
    }

    // margin in pixels
    readonly MARGIN: number = 5;

    // photos in the collection
    @Input()
    photos: RenderedPhoto[];

    // photos arranged in rows (see layout()) 
    photoRows: RenderedPhoto[][] = new Array();

    constructor( private photoService: PhotoService, public changeDetectorRef: ChangeDetectorRef ) {
    }

    ngOnChanges() {
        console.log( "PhotoGalleryComponent.onChanges() ---->" );
        this.layout();
        console.log( "PhotoGalleryComponent.onChanges() <----" );
    }

    /**
     * Arrange the photos into rows 
     */
    private layout() {
        this.photoRows = new Array();

        let curRow: RenderedPhoto[] = new Array();
        let curRowWidth: number = 0;
        let curRowHeight: number = 0;
        // we will always have at least one row, push the first row.
        this.photoRows.push( curRow );

        // "Responsive" -- the ideal number of photos per row depends on the wrapper width
        let numPhotosPerRow: number;
        let wrapperWidth: number = this.wrapper.nativeElement.clientWidth;
        if ( wrapperWidth >= 1024 ) {
            numPhotosPerRow = 3;
        } else if ( wrapperWidth >= 600 ) {
            numPhotosPerRow = 2;
        } else {
            numPhotosPerRow = 1;
        }

        // calculate the ideal image size based on landscape orientation 3:2 aspect
        let nominalWidth: number = ( wrapperWidth - this.MARGIN * 2 ) / numPhotosPerRow;
        let nominalHeight: number = nominalWidth * 0.666;
        let nominalArea: number = nominalWidth * nominalHeight;

        for ( let photo of this.photos ) {
            let bestResource: ImageResource = photo.getBestResourceByArea( nominalArea );

            // scale factor of the best resource to nominal height
            let imageScale: number = nominalHeight / bestResource.height;
            photo.width = bestResource.width * imageScale;
            photo.height = bestResource.height * imageScale;

            let newWidth: number = curRowWidth + photo.width;
            if ( curRow.length > 0 ) {
                newWidth += this.MARGIN;
            }

            // difference between row width and actual width
            let newDiff = Math.abs( newWidth - wrapperWidth );
            let oldDiff = Math.abs( curRowWidth - wrapperWidth );

            //            console.log( "===" );
            //            console.log( "photo: " + photo.title );
            //            console.log( "nominal width: " + nominalWidth );
            //            console.log( "nominalArea: " + nominalArea );
            //            console.log( "imageScale: " + imageScale );
            //            console.log( "bestResource: " + bestResource.url );
            //            console.log( "bestResource.width: " + bestResource.width );
            //            console.log( "photo.width: " + photo.width );
            //            console.log( "curRowWidth: " + curRowWidth );
            //            console.log( "newWidth: " + newWidth );
            //            console.log( "newDiff: " + newDiff );
            //            console.log( "oldDiff: " + oldDiff );

            // if this photo DOESN'T fit in curRow
            // (which is to say, if the natural width of the old row is closer to ideal) 
            if ( newDiff > oldDiff ) {
                // start a new row.
                curRow = new Array();
                this.photoRows.push( curRow );
                curRowWidth = 0;
                console.log( ">> New Row." );
            }

            // Add the photo to curRow
            curRow.push( photo );
            curRowWidth += photo.width;
            if ( curRow.length > 1 ) {
                newWidth += this.MARGIN;
            }
        }

        this.resize();
    }

    /**
     * resize each row (by resizing all the photos in it)  to fit the screen
     */
    private resize() {
        for ( let photoRow of this.photoRows ) {
            // calculate unscaled row width
            let rowWidth: number = 0;
            for ( let photo of photoRow ) {
                rowWidth += photo.width;
            }
            if ( photoRow.length > 1 ) {
                rowWidth += this.MARGIN * ( photoRow.length - 1 );
            }


            // determine the scale factor for the row
            let rowScale: number;
            if ( photoRow === this.photoRows[this.photoRows.length - 1] ) {
                // if this is  the last row, no scaling
                rowScale = 1.0;
            } else {
                // all other rows scale to fit width
                rowScale = this.wrapper.nativeElement.clientWidth / rowWidth;
            }

            // apply to the RenderedPhoto dimension
            for ( let photo of photoRow ) {
                photo.width *= rowScale;
                photo.height *= rowScale;
            }
        }

        this.changeDetectorRef.detectChanges()
    }
}