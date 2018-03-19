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
import {
    AfterViewInit, ChangeDetectorRef, Component, ElementRef, HostListener, Input,
    OnChanges, OnInit, Output, QueryList, ViewChild, ViewChildren
} from '@angular/core';
import { Location } from '@angular/common';
import { RenderedPhoto } from '../../model/rendered-photo';
import { ImageResource } from '../../model/image-resource';
import { PhotoService } from '../../services/photo.service';
import { ThumbnailComponent } from '../thumbnail/thumbnail.component';

@Component( {
    selector: 'app-photo-gallery',
    templateUrl: './photo-gallery.component.html',
    styleUrls: ['./photo-gallery.component.css']
} )
export class PhotoGalleryComponent implements OnInit, AfterViewInit {
    @ViewChild( 'wrapper' ) wrapper: ElementRef;
    @ViewChildren( 'photoChild' ) photoElements: QueryList<ThumbnailComponent>;

    // margin in pixels
    readonly MARGIN: number = 5;

    // photos in the collection
    @Input()
    photos: RenderedPhoto[];

    /**
     * Approximate number of columns for wide layout
     */
    @Input()
    colsMax: number = 3;

    /**
     * Approximate number of columns for medium layout
     */
    @Input()
    colsMed: number = 2;

    /**
     * Approximate number of columns for narrow layout
     */
    @Input()
    colsMin: number = 1;

    /**
     * Maximum number of rows, no matter how many photos in the gallery.
     * "0" means "unlimited".
     */
    @Input()
    rowsMax: number = 0;

    photosById: Map<number, RenderedPhoto>;
    indexById: Map<number, number>;

    // photos arranged in rows (see layout())
    photoRows: RenderedPhoto[][] = new Array();

    @HostListener( 'window:scroll', ['$event'] ) triggerCycle( event: any ) {
        this.resize();
        this.doDelayedLoad();
    }

    @HostListener( 'window:resize', ['$event'] ) windowResize( event: any ) {
        this.layout(); // includes resize
        this.doDelayedLoad();
    }

    constructor( private photoService: PhotoService, private changeDetectorRef: ChangeDetectorRef, private location: Location ) {
        console.log( 'gallery path: ' + location.path() );
    }

    ngOnInit() {
        this.photosById = new Map();
        this.indexById = new Map();
        let i: number = 0;
        for ( const photo of this.photos ) {
            photo.isLoaded = false; // no autoload
            this.photosById.set( photo.id, photo );
            this.indexById.set( photo.id, i );
            i++;
        }
        this.layout();
    }

    ngAfterViewInit() {
        setTimeout(() => {
            // delayed load works on the photoElements which are not available until AfterViewInit
            this.doDelayedLoad();
        } );
    }

    /**
     * Arrange the photos into rows
     */
    private layout() {
        console.log( 'PhotoGalleryComponent.layout() ---->  this.photos: ' + this.photos );
        this.photoRows = new Array();

        let curRow: RenderedPhoto[] = new Array();
        let curRowWidth: number = 0;
        // we will always have at least one row, push the first row.
        this.photoRows.push( curRow );

        // "Responsive" -- the ideal number of photos per row depends on the wrapper width
        const numPhotosPerRow: number = this.getResponsiveColumns();
        const wrapperWidth: number = this.wrapper.nativeElement.clientWidth;
        let rowCount: number = 1;

        // calculate the ideal image size based on landscape orientation 3:2 aspect
        const nominalWidth: number = ( wrapperWidth - this.MARGIN * 2 ) / numPhotosPerRow;
        const nominalHeight: number = nominalWidth * 0.666;
        const nominalArea: number = nominalWidth * nominalHeight;

        for ( const photo of this.photos ) {
            const bestResource: ImageResource = photo.getBestResourceByArea( nominalArea );

            // scale factor of the best resource to nominal height
            const imageScale: number = nominalHeight / bestResource.height;
            photo.width = bestResource.width * imageScale;
            photo.height = bestResource.height * imageScale;

            let newWidth: number = curRowWidth + photo.width;
            if ( curRow.length > 0 ) {
                newWidth += this.MARGIN;
            }

            // difference between row width and actual width
            const newDiff = Math.abs( newWidth - wrapperWidth );
            const oldDiff = Math.abs( curRowWidth - wrapperWidth );

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
            photo.isVisible = true;
            if ( newDiff > oldDiff ) {
                if ( this.rowsMax === 0 || rowCount < this.rowsMax ) {
                    // start a new row.
                    console.log( '>> New Row. rowCount: ' + rowCount + ' (of ' + this.rowsMax + ')' );
                    rowCount++;
                    curRow = new Array();
                    this.photoRows.push( curRow );
                    curRowWidth = 0;
                } else {
                    photo.isVisible = false;
                }
            }

            if ( photo.isVisible ) {
                // Add the photo to curRow
                curRow.push( photo );
                curRowWidth += photo.width;
                if ( curRow.length > 1 ) {
                    newWidth += this.MARGIN;
                }
            }
        }

        this.resize();
    }

    /**
     * resize each row (by resizing all the photos in it)  to fit the screen
     */
    private resize() {
        console.log( 'PhotoGalleryComponent.resize() ---->' );
        for ( const photoRow of this.photoRows ) {
            // calculate unscaled row width
            let rowWidth: number = 0;
            for ( const photo of photoRow ) {
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
            for ( const photo of photoRow ) {
                photo.width *= rowScale;
                photo.height *= rowScale;
            }
        }

        // reflect changes back to the view
        this.changeDetectorRef.detectChanges();
    }

    /**
     * Flip each photo's isLoaded flag based on whether the photo has ever been on screen
     */
    private doDelayedLoad(): void {
        if ( this.photoElements && this.photos ) {
            for ( const element of this.photoElements.toArray() ) {
                element.doDelayedLoad();
            }
        }
    }

    /**
     * Return the ideal number of columns given the width of the browser viewport.
     */
    private getResponsiveColumns(): number {
        let result: number;
        const wrapperWidth: number = this.wrapper.nativeElement.clientWidth;
        if ( wrapperWidth >= 1024 ) {
            result = this.colsMax;
        } else if ( wrapperWidth >= 600 ) {
            result = this.colsMed;
        } else {
            result = this.colsMin;
        }

        return result;
    }
}
