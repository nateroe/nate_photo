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
import { ChangeDetectorRef, Component, ElementRef, HostListener, OnInit, ViewChild } from '@angular/core';

import { Expedition } from '../../model/expedition';
import { RenderedPhoto } from '../../model/rendered-photo';
import { ImageResource } from '../../model/image-resource';
import { ExpeditionService } from '../../services/expedition.service';
import { PhotoService } from '../../services/photo.service';

@Component( {
    selector: 'app-expedition-gallery',
    templateUrl: './expedition-gallery.component.html',
    styleUrls: ['./expedition-gallery.component.css']
} )
export class ExpeditionGalleryComponent implements OnInit {
    @ViewChild( 'wrapper' ) wrapper: ElementRef

    @HostListener( 'window:resize', ['$event'] ) windowResize( event: any ) {
        this.layout(); // includes resize
    }

    // margin in pixels
    readonly MARGIN: number = 5;

    expeditions: Expedition[];
    expeditionHighlights: Map<number, RenderedPhoto[]>;

    constructor(
        public changeDetectorRef: ChangeDetectorRef,
        private expeditionService: ExpeditionService,
        private photoService: PhotoService ) {
    }

    ngOnInit(): void {
        console.log( "subscribe to expeditions" );
        this.expeditionService.getAllExpeditions().subscribe(
            data => {
                this.expeditions = data;
                this.sortExpeditions();
                this.expeditionHighlights = new Map();
                for ( let expedition of this.expeditions ) {
                    this.getHighlights( expedition.id );
                }
            } );
    }


    getHighlights( expeditionId: number ): void {
        this.photoService.getPhotoHighlightsByExpedition( expeditionId ).subscribe(
            data => {
                this.expeditionHighlights.set( expeditionId, data );
                this.layout();
            }
        );
    }

    /**
     * Sort expeditions by date, descending (most recent first)
     */
    sortExpeditions(): void {
        this.expeditions.sort(( a: Expedition, b: Expedition ) => {
            return b.beginDate.getTime() - a.beginDate.getTime();
        } );
    }

    /**
     * resize each row of expedition highlights (by resizing all the photos in it)  to fit the screen
     * (similar to PhotoGallery because we are (maybe) stretching rows to fit the screen. But we are 
     * also hiding highlight photos that don't fit in one row.
     */
    private layout(): void {
        let curRowWidth: number = 0;
        let curRowHeight: number = 0;

        // calculate the ideal image size based on landscape orientation 3:2 aspect
        let wrapperWidth: number = this.wrapper.nativeElement.clientWidth;
        let nominalWidth: number = ( wrapperWidth - this.MARGIN * 2 ) / this.getResponsiveColumns();
        let nominalHeight: number = nominalWidth * 0.666;
        let nominalArea: number = nominalWidth * nominalHeight;

        for ( let expedition of this.expeditions ) {
            let highlights: RenderedPhoto[] = this.expeditionHighlights.get( expedition.id );
            if ( highlights ) {
                curRowWidth = 0;
                for ( let photo of highlights ) {
                    let bestResource: ImageResource = photo.getBestResourceByArea( nominalArea );

                    // scale factor of the best resource to nominal height
                    let imageScale: number = nominalHeight / bestResource.height;

                    // calculate the scaled rendered size
                    photo.width = bestResource.width * imageScale;
                    photo.height = bestResource.height * imageScale;

                    let newWidth: number = curRowWidth + photo.width;
                    // if this is not the first photo in the row
                    if ( photo !== highlights[0] ) {
                        newWidth += this.MARGIN;
                    }

                    // difference between row width and browser width
                    // (larger difference, more scaling)
                    let newDiff = Math.abs( newWidth - wrapperWidth );
                    let oldDiff = Math.abs( curRowWidth - wrapperWidth );

                    // photo is visible if it fits in curRow
                    // (less scaling is better)
                    photo.isVisible = newDiff <= oldDiff;
                    if ( photo.isVisible ) {
                        if ( curRowWidth > 0 ) {
                            curRowWidth += this.MARGIN;
                        }
                        curRowWidth += photo.width;
                    }
                }
            }
        }

        this.resize();
    }

    /**
     * resize each row (by resizing all the photos in it)  to fit the screen
     */
    private resize(): void {
        for ( let expedition of this.expeditions ) {
            // contains only visible highlights
            let highlights: RenderedPhoto[] = this.expeditionHighlights.get( expedition.id );
            //           let highlights: RenderedPhoto[] = this.expeditionHighlights.get( expedition.id ).filter( photo => photo.isVisible );
            if ( highlights ) {
                // calculate unscaled row width
                let rowWidth: number = 0;
                let visibleHighlights: number = 0;
                for ( let photo of highlights ) {
                    if ( photo.isVisible ) {
                        rowWidth += photo.width;
                        visibleHighlights++;
                    }
                }

                if ( visibleHighlights > 1 ) {
                    rowWidth += this.MARGIN * ( visibleHighlights - 1 );
                }

                // determine the scale factor for the row
                let rowScale: number;
                // try to fit width
                rowScale = this.wrapper.nativeElement.clientWidth / rowWidth;
                if ( Math.abs( rowScale - 1.0 ) > 0.5 ) {
                    // if we are stretching too much, no scaling
                    rowScale = 1.0;
                }

                // apply to the RenderedPhoto dimension
                for ( let photo of highlights ) {
                    if ( photo.isVisible ) {
                        photo.width *= rowScale;
                        photo.height *= rowScale;
                    }
                }
                console.log( "------" );
                console.log( "this.wrapper.nativeElement.clientWidth: " + this.wrapper.nativeElement.clientWidth );
                console.log( "rowWidth: " + rowWidth );
            }
        }
        this.changeDetectorRef.detectChanges()
    }

    private getResponsiveColumns() {
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


        return numPhotosPerRow;
    }
}
