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
import { environment } from '../../environments/environment';
import { ImageResource } from './image-resource';

/**
 * Corresponds to the back end Photo. Contains EXIF data and a list (an Array here) of ImageResources.
 */
export class Photo {
    id: number;
    title: string;
    description: string;
    rating: number;
    date: Date;
    camera: string;
    lens: string;
    aperture: string;
    shutterSpeed: string;
    iso: string;
    flash: number;
    focalLength: number;
    focusDistance: number;
    latitude: number;
    longitude: number;
    altitude: number;
    copyright: string;
    usageTerms: string;
    isPublished: boolean;
    images: ImageResource[];

    copyFrom( that: Photo ): Photo {
        Object.assign( this, that );
        this.date = new Date( that.date );

        this.images = new Array();
        for ( let thatImage of that.images ) {
            let thisImage = new ImageResource();
            Object.assign( thisImage, thatImage );
            this.images.push( thisImage );
        }

        return this;
    }

    getBestResourceByArea( area: number ): ImageResource {
        let result: ImageResource = null;

        for ( let image of this.images ) {
            result = image;
            // find first image smaller than or equal to one and a half times the requested area
            // (mipmap transition occurs halfway to next size)
            if ( area * 1.5 >= image.width * image.height ) {
                //                console.log( "Make a selection!" );
                break;
            }
        }

        if ( result && !result.url.startsWith( environment.restBaseUrl ) ) {
            result.url = environment.restBaseUrl + result.url;
        }

        return result;
    }


    getFlashString(): string {
        return ( ( this.flash & 0x01 ) != 0 ) ? "Yes" : "No";
    }
}