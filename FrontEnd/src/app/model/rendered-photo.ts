import { Photo } from './photo';
import { ImageResource } from './image-resource';

/**
 * A rendered Photo is a Photo that is rendered at some on-screen size. 
 */
export class RenderedPhoto extends Photo {
    width: number;
    height: number;

    copyFrom( that: Photo ): RenderedPhoto {
        super.copyFrom( that );
        // width and height are ignored because "that" is a Photo (not RenderedPhoto)
        return this;
    }

    /**
     * Return the best ImageResource for this RenderedPhoto's intrinsic size
     */
    getBestResource(): ImageResource {
        return super.getBestResourceByArea( this.width * this.height );
    }
}