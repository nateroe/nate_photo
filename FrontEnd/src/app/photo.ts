import { environment } from '../environments/environment';
import { ImageResource } from './image_resource';

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

        this.images = new Array();
        for ( let thatImage of that.images ) {
            let thisImage = new ImageResource();
            Object.assign( thisImage, thatImage );
            this.images.push( thisImage );
        }

        return this;
    }

    getBestResource( width: number, height: number ): ImageResource {
        console.log( "----> photo.getBestResource(...)" );
        let result: ImageResource = null;
        for ( let image of this.images ) {
            result = image;
            console.log( "Examine image resource " + result.url + " (req area: " + ( width * height ) + " >= image area: " + ( image.width * image.height ) + ")" );
            // find first image smaller than or equal to the requested area
            if ( width * height >= image.width * image.height ) {
                console.log( "Make a selection!" );
                break;
            }
        }

        if ( !result.url.startsWith( 'environment.restBaseUrl' ) ) {
            result.url = environment.restBaseUrl + result.url;
        }

        console.log( "----< Returning best image resource " + result.url + " (width: " + width + " >= " + result.width );
        return result;
    }


    getFlashString(): string {
        return ( ( this.flash & 0x01 ) != 0 ) ? "Yes" : "No";
    }
}