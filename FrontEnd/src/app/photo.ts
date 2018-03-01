export class Photo {
    id: number;
    title: string;
    description: string;
    rating: number;
    date: Date;
    camera: string;
    lens: string;
    aperture: string;
    iso: string;
    shutterSpeed: string;
    isFlashFired: boolean;
    focusDistance: number;
    copyright: string;
    isMakingOf: boolean;
    isPublished: boolean;
    images: ImageResource[];

    /**
     * Assuming that images is ordered by decreasing size, choose the largest image that fits in the requested area.
     * 
     * @param width
     * @param height
     */
    getBestResource( width: number, height: number ): ImageResource {
        var result: ImageResource;
        for ( let image of this.images ) {
            result = image;
            if ( width * height > image.width * image.height ) {
                break;
            }
        }
        
        console.log("Returning best image resource " + result.url);
        
        return result;
    }
}