import { Photo } from '../photo';
import { RenderedPhoto } from '../rendered-photo';
import { PHOTO, PHOTOS } from './mock-photo';

export const RENDERED_PHOTO: RenderedPhoto = new RenderedPhoto().copyFrom( PHOTO );

export const RENDERED_PHOTOS: RenderedPhoto[] =
    [
        new RenderedPhoto().copyFrom( PHOTOS[0] ),
        new RenderedPhoto().copyFrom( PHOTOS[1] ),
        new RenderedPhoto().copyFrom( PHOTOS[2] )
    ];

