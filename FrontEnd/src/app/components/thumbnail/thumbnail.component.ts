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
import { ChangeDetectorRef, Component, ElementRef, Input, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { RenderedPhoto } from '../../model/rendered-photo';
import { GalleryContextService } from '../../services/gallery-context.service';

/**
 * A ThumbnailComponent encapsulates the behavior of a thumbnail within a gallery.
 * - delayed loading
 * - mouse hover
 * - link activation (storing the gallery context during navigation)
 */
@Component( {
    selector: 'app-thumbnail',
    templateUrl: './thumbnail.component.html',
    styleUrls: ['./thumbnail.component.css']
} )
export class ThumbnailComponent implements OnInit {
    @ViewChild( 'photoChild' ) photoElement: ElementRef;

    @Input()
    photo: RenderedPhoto;

    @Input()
    galleryUrl: string;

    @Input()
    photoIndex: number;

    @Input()
    photos: RenderedPhoto[];

    isMouseOver: boolean = false;

    constructor( private router: Router, private galleryContextService: GalleryContextService ) { }

    ngOnInit() {
    }

    mouseEnter(): void {
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
            const top: number = this.photoElement.nativeElement.getBoundingClientRect().top;
            const bottom: number = this.photoElement.nativeElement.getBoundingClientRect().bottom;

            // The element is on screen if either the top of the image is within the window,
            // or the bottom of the image is within the window,
            // or the top of the image is above the window and the bottom of the image is below.
            const isOnScreen: boolean = ( top >= 0 && top < window.innerHeight )
                || ( bottom >= 0 && bottom < window.innerHeight )
                || ( top < 0 && bottom >= window.innerHeight );

            this.photo.isLoaded = this.photo.isLoaded || isOnScreen;
        } else {
            console.log( 'No photo for ' + this.photo.id );
        }
    }

    doClick() {
        const galleryContextId: number = this.galleryContextService.registerGalleryClick( this.galleryUrl, this.photoIndex, this.photos );
        console.log( 'galleryContextId: ' + galleryContextId );
        this.router.navigate( ['/photo/' + this.photo.id], { queryParams: { contextId: galleryContextId } } );
    }
}
