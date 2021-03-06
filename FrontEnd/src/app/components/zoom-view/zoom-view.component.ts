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
    ChangeDetectorRef, Component, ElementRef, EventEmitter, HostListener, Input,
    OnInit, Output, OnChanges, ViewChild
} from '@angular/core';
import { DomSanitizer, SafeStyle, SafeUrl } from '@angular/platform-browser';

import { RenderedPhoto } from '../../model/rendered-photo';
import { ImageResource } from '../../model/image-resource';
import { environment } from '../../../environments/environment';

/**
 * View a given photo at the highest res, panning around the view as the mouse moves.
 * Dismissed with a click or any key press.
 */
@Component( {
    selector: 'app-zoom-view',
    templateUrl: './zoom-view.component.html',
    styleUrls: ['./zoom-view.component.css']
} )
export class ZoomViewComponent implements OnChanges {
    @ViewChild( 'wrapper' ) wrapper: ElementRef;

    @Input()
    photo: RenderedPhoto;

    @Input()
    viewX: number;

    @Input()
    viewY: number;

    @Output()
    dismissedEvent = new EventEmitter<void>();

    bestResource: ImageResource;

    // XXX this safe URL stuff is a hack for my dev environment where my server is running elsewhere.
    safeUrl: SafeUrl;

    offsetX: number = 0;
    offsetY: number = 0;

    wrapperOffsetX: number = 0;
    wrapperOffsetY: number = 0;

    @HostListener( 'window:scroll', ['$event'] ) triggerCycle( event: any ) {
        this.doWrapperOffset();
    }

    constructor( private sanitizer: DomSanitizer, private changeDetectorRef: ChangeDetectorRef ) {
    }

    ngOnChanges(): void {
        if ( this.photo && this.photo.images ) {
            // best resource is biggest available resource
            this.bestResource = this.photo.images[0];
            if ( this.bestResource ) {
                if ( !this.bestResource.url.startsWith( environment.restBaseUrl ) ) {
                    this.safeUrl = this.sanitizer.bypassSecurityTrustUrl( environment.restBaseUrl + this.bestResource.url );
                } else {
                    this.safeUrl = this.sanitizer.bypassSecurityTrustUrl( this.bestResource.url );
                }

                for ( const image of this.photo.images ) {
                    console.log( 'image: ' + image.url );
                }

                this.doOffset( this.viewX, this.viewY );
            }
        }

        this.doWrapperOffset();
    }

    getWidth(): number {
        return this.bestResource.width;
    }

    getHeight(): number {
        return this.bestResource.height;
    }

    mouseMove( event: MouseEvent ): void {
        this.doOffset( event.clientX, event.clientY );
    }

    touchMove( event: TouchEvent ): void {
        this.doOffset( event.touches.item( 0 ).clientX, event.touches.item( 0 ).clientY );
        event.preventDefault();
    }

    doOffset( mouseX: number, mouseY: number ): void {
        const screenWidth: number = window.innerWidth;
        const screenHeight: number = window.innerHeight;

        // normalize mouse coords
        let nmx: number = mouseX / screenWidth;
        let nmy: number = mouseY / screenHeight;

        // enlarge mouse area
        nmx = ( nmx - 0.5 ) * 1.15 + 0.5;
        nmy = ( nmy - 0.5 ) * 1.15 + 0.5;

        // find the relative offsetX
        if ( screenWidth <= this.bestResource.width ) {
            // if the photo is bigger than the screen, use mouse position
            this.offsetX = ( screenWidth - this.bestResource.width ) * nmx;
            // ensure that the top and left boundaries of the larger image are never exceeded
            this.offsetX = Math.min( 0, this.offsetX );
            // ensure that the right and bottom boundaries are respected
            this.offsetX = Math.max( this.offsetX, screenWidth - this.bestResource.width );
        } else {
            // if the photo is smaller than the screen, center it
            this.offsetX = ( screenWidth - this.bestResource.width ) * 0.5;
        }

        // find the relative offsetY
        if ( screenHeight <= this.bestResource.height ) {
            // if the photo is bigger than the screen, use mouse position
            this.offsetY = ( screenHeight - this.bestResource.height ) * nmy;
            this.offsetY = Math.min( 0, this.offsetY );
            this.offsetY = Math.max( this.offsetY, screenHeight - this.bestResource.height );
        } else {
            // if the photo is smaller than the screen, center it
            this.offsetY = ( screenHeight - this.bestResource.height ) * 0.5;
        }

        //        console.log( '-----------------------------------' );
        //        console.log( 'mouse position: ' + mouseX + ', ' + mouseY );
        //        console.log( 'screen size: ' + screenWidth + ', ' + screenHeight );
        //        console.log( 'window.inner: ' + window.innerWidth + ', ' + window.innerHeight );
        //        console.log( 'window.devicePixelRatio: ' + window.devicePixelRatio );
        //        console.log( 'bestResource size: ' + this.bestResource.width + ', ' + this.bestResource.height );
        //        console.log( 'normalize mouse coords: ' + nmx + ', ' + nmy );
        //        console.log( 'offset: ' + this.offsetX + ', ' + this.offsetY );
    }

    doWrapperOffset() {
        if ( this.wrapper ) {
            this.wrapperOffsetX = window.scrollX;
            this.wrapperOffsetY = window.scrollY;
        }
    }
}
