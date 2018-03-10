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

    sortExpeditions(): void {
        this.expeditions.sort(( a: Expedition, b: Expedition ) => {
            return a.beginDate.getTime() - b.beginDate.getTime();
        } );
    }

    /**
     * resize each row of expedition highlights (by resizing all the photos in it)  to fit the screen
     * (similar to PhotoGallery because we are (maybe) stretching rows to fit the screen. But we are 
     * also hiding highlight photos that don't fit in one row.
     */
    private layout(): void {
        console.log( "layout--->" );
        let curRowWidth: number = 0;
        let curRowHeight: number = 0;

        // calculate the ideal image size based on landscape orientation 3:2 aspect
        let wrapperWidth: number = this.wrapper.nativeElement.clientWidth;
        let nominalWidth: number = ( wrapperWidth - this.MARGIN * 2 ) / this.getResponsiveColumns();
        let nominalHeight: number = nominalWidth * 0.666;
        let nominalArea: number = nominalWidth * nominalHeight;

        for ( let expedition of this.expeditions ) {
            console.log( "layout--->1" );
            let highlights: RenderedPhoto[] = this.expeditionHighlights.get( expedition.id );
            if ( highlights ) {
                console.log( "expedition.id: " + expedition.id );
                console.log( "highlights: " + highlights );
                if ( highlights ) {
                    console.log( "highlights.length: " + highlights.length );
                }

                console.log( "layout--->2" );

                for ( let photo of highlights ) {
                    console.log( "layout--->3" );
                    let bestResource: ImageResource = photo.getBestResourceByArea( nominalArea );

                    // scale factor of the best resource to nominal height
                    let imageScale: number = nominalHeight / bestResource.height;
                    photo.width = bestResource.width * imageScale;
                    photo.height = bestResource.height * imageScale;

                    let newWidth: number = curRowWidth + photo.width;
                    // if this is not the first photo in the row
                    if ( photo !== highlights[0] ) {
                        newWidth += this.MARGIN;
                    }

                    // difference between row width and actual width
                    let newDiff = Math.abs( newWidth - wrapperWidth );
                    let oldDiff = Math.abs( curRowWidth - wrapperWidth );

                    // if this photo DOESN'T fit in curRow
                    if ( newDiff > oldDiff ) {
                        // hide it
                        photo.isVisible = false;
                    }
                }
            }
        }

        this.resize();
        console.log( "layout---<" );
    }



    private resize(): void {
        console.log( "resize--->" );
        for ( let expedition of this.expeditions ) {
            // contains only visible highlights
            let highlights: RenderedPhoto[] = this.expeditionHighlights.get( expedition.id );
            //           let highlights: RenderedPhoto[] = this.expeditionHighlights.get( expedition.id ).filter( photo => photo.isVisible );
            if ( highlights ) {

                // calculate unscaled row width
                let rowWidth: number = 0;
                for ( let photo of highlights ) {
                    rowWidth += photo.width;
                }
                if ( highlights.length > 1 ) {
                    rowWidth += this.MARGIN * ( highlights.length - 1 );
                }

                // determine the scale factor for the row
                let rowScale: number;
                // try to fit width
                rowScale = this.wrapper.nativeElement.clientWidth / rowWidth;
                if ( Math.abs( rowScale - 1.0 ) > 0.5 ) {
                    // if we are stretching too much, no scaling
                    rowScale = 1.0;
                }
            }
        }
        this.changeDetectorRef.detectChanges()
        console.log( "resize---<" );
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
