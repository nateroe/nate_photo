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
import { Photo } from './photo';
import { ImageResource } from './image-resource';

/**
 * A rendered Photo is a Photo that is rendered at some on-screen size.
 */
export class RenderedPhoto extends Photo {
    width: number;
    height: number;
    isVisible: boolean = true;
    isLoaded: boolean = true;

    copyFrom( that: Photo ): RenderedPhoto {
        super.copyFrom( that );
        // width and height are ignored because "that" is a Photo (not RenderedPhoto)
        return this;
    }

    /**
     * Return the best ImageResource for this RenderedPhoto's intrinsic size
     * (or null if not isVisible or not isOnScreen).
     */
    getBestResourceUrl(): string {
        const best: ImageResource =
            ( !this.isVisible || !this.isLoaded ) ? null : super.getBestResourceByArea( this.width * this.height );
        const url: string = best == null ? '' : best.url;
        return url;
    }
}
