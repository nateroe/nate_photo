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
import { Photo } from '../model/photo';

export const PHOTO: Photo = Object.assign( new Photo(), {
    'id': 1,
    'title': 'Sunset on the Olympic Coast',
    'description': 'Sunset on the Olympic Coast. Olympic National Park, Washington.',
    'rating': 5,
    'date': new Date( 1503026212000 ),
    'camera': 'Canon EOS 5D Mark II',
    'lens': 'EF24-70mm f/2.8L II USM',
    'aperture': '22',
    'shutterSpeed': '2.0',
    'iso': 'ISO 50',
    'isFlashFired': false,
    'focusDistance': 300,
    'copyright': '©2017 Nathaniel Roe',
    'isMakingOf': false,
    'isPublished': true,
    'images': [
        {
            'id': 1,
            'url': 'http://phlake.org/natephoto/IMG_6711-2-Edit-1.jpg',
            'width': 5510,
            'height': 3645
        },
        {
            'id': 2,
            'url': 'http://phlake.org/natephoto/IMG_6711-2-Edit-2.jpg',
            'width': 2048,
            'height': 1355
        },
        {
            'id': 3,
            'url': 'http://phlake.org/natephoto/IMG_6711-2-Edit-3.jpg',
            'width': 1024,
            'height': 677
        },
        {
            'id': 4,
            'url': 'http://phlake.org/natephoto/IMG_6711-2-Edit-4.jpg',
            'width': 512,
            'height': 339
        },
        {
            'id': 5,
            'url': 'http://phlake.org/natephoto/IMG_6711-2-Edit-5.jpg',
            'width': 256,
            'height': 169
        },
        {
            'id': 6,
            'url': 'http://phlake.org/natephoto/IMG_6711-2-Edit-6.jpg',
            'width': 128,
            'height': 85
        }
    ]
} );
