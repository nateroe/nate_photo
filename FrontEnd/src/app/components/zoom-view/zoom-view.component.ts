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
import { ChangeDetectorRef, Component, EventEmitter, Input, OnInit, Output, OnChanges } from '@angular/core';
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
    @Input()
    photo: RenderedPhoto;

    @Input()
    isVisible: boolean = false;

    @Input()
    viewX: number;

    @Input()
    viewY: number;

    @Output()
    dismissedEvent = new EventEmitter<void>();

    bestResource: ImageResource;

    // XXX this safe URL stuff is a hack for my dev environment where my server is running elsewhere.
    safeStyle: SafeStyle;
    safeUrl: SafeUrl;

    offsetX: number = 0;
    offsetY: number = 0;

    constructor( private sanitizer: DomSanitizer, private changeDetectorRef: ChangeDetectorRef ) {
    }

    ngOnChanges(): void {
        if ( this.photo && this.photo.images ) {
            this.bestResource = this.photo.images[0];
            if ( this.bestResource ) {
                if ( !this.bestResource.url.startsWith( environment.restBaseUrl ) ) {
                    this.safeStyle = this.sanitizer.bypassSecurityTrustStyle( environment.restBaseUrl + this.bestResource.url );
                    this.safeUrl = this.sanitizer.bypassSecurityTrustUrl( environment.restBaseUrl + this.bestResource.url );
                } else {
                    this.safeStyle = this.sanitizer.bypassSecurityTrustStyle( this.bestResource.url );
                    this.safeUrl = this.sanitizer.bypassSecurityTrustUrl( this.bestResource.url );
                }

                for ( const image of this.photo.images ) {
                    console.log( 'image: ' + image.url );
                }
            }
        }
    }

    getWidth(): number {
        return this.bestResource.width;
    }

    getHeight(): number {
        return this.bestResource.height;
    }

    move( event: MouseEvent ): void {
        // normalize mouse coords
        const nmx: number = event.clientX / window.innerWidth;
        const nmy: number = event.clientY / window.innerHeight;

        // find the relative offset
        this.offsetX = ( window.innerWidth - this.bestResource.width ) * nmx;
        this.offsetY = ( window.innerHeight - this.bestResource.height ) * nmy;

        this.offsetX += window.pageXOffset;
        this.offsetY += window.pageYOffset;

        //        console.log( '-----------------------------------' );
        //        console.log( 'mouse position: ' + event.clientX + ', ' + event.clientY );
        //        console.log( 'screen size: ' + window.innerWidth + ', ' + window.innerHeight );
        //        console.log( 'bestResource size: ' + this.bestResource.width + ', ' + this.bestResource.height );
        //        console.log( 'normalize mouse coords: ' + nmx + ', ' + nmy );
        //        console.log( 'offset: ' + this.offsetX + ', ' + this.offsetY );
    }
}
