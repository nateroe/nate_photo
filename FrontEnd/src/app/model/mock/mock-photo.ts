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
import { Photo } from '../photo';

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


export const PHOTOS: Photo[] = [
    Object.assign( new Photo(),
        {
            'id': 69,
            'title': 'Brice Creek',
            'description': 'Brice Creek in the Umpqua National Forest, Oregon.',
            'rating': 3,
            'date': 1506020530000,
            'camera': 'Canon EOS 5D Mark II',
            'lens': 'EF24-70mm f/2.8L II USM',
            'aperture': '4',
            'shutterSpeed': '4',
            'iso': '50',
            'flash': 16,
            'focalLength': 25,
            'focusDistance': 4.71,
            'latitude': null,
            'longitude': null,
            'altitude': null,
            'copyright': '©2017 Nathaniel Roe',
            'usageTerms': 'All Rights Reserved',
            'isPublished': true,
            'expeditionId': 1,
            'images': [
                {
                    'id': 187,
                    'url': '/res/8663c718-b952-4581-8f13-f8c269f0314c.jpg',
                    'fileName': null,
                    'width': 2048,
                    'height': 1365
                },
                {
                    'id': 188,
                    'url': '/res/3390adc1-25ae-4e98-b4b1-253924ecc14b.jpg',
                    'fileName': null,
                    'width': 1024,
                    'height': 682
                },
                {
                    'id': 189,
                    'url': '/res/ca32d0d8-9574-415a-9c35-a7dbd9d2fa4c.jpg',
                    'fileName': null,
                    'width': 512,
                    'height': 341
                },
                {
                    'id': 190,
                    'url': '/res/2a7968eb-dabd-42b1-8240-af9fc14d4731.jpg',
                    'fileName': null,
                    'width': 256,
                    'height': 170
                }
            ]
        } ),
    Object.assign( new Photo(), {
        'id': 60,
        'title': 'Brice Creek',
        'description': 'Brice Creek in the Umpqua National Forest, Oregon.',
        'rating': 3,
        'date': 1506019631000,
        'camera': 'Canon EOS 5D Mark II',
        'lens': 'EF24-70mm f/2.8L II USM',
        'aperture': '2.8',
        'shutterSpeed': '1/2',
        'iso': '50',
        'flash': 16,
        'focalLength': 24,
        'focusDistance': 7.32,
        'latitude': null,
        'longitude': null,
        'altitude': null,
        'copyright': '©2017 Nathaniel Roe',
        'usageTerms': 'All Rights Reserved',
        'isPublished': true,
        'expeditionId': 1,
        'images': [
            {
                'id': 142,
                'url': '/res/d8b27e4a-67f8-460c-9467-3731f1826f8c.jpg',
                'fileName': null,
                'width': 1365,
                'height': 2048
            },
            {
                'id': 143,
                'url': '/res/f8ee236d-c1a0-4521-889f-a06390a614f0.jpg',
                'fileName': null,
                'width': 682,
                'height': 1024
            },
            {
                'id': 144,
                'url': '/res/3fbbf342-6d17-4802-b6c5-db4685884600.jpg',
                'fileName': null,
                'width': 341,
                'height': 512
            },
            {
                'id': 145,
                'url': '/res/00e90a9a-4c57-448e-8e4a-d7a435ddf46d.jpg',
                'fileName': null,
                'width': 170,
                'height': 256
            }
        ]
    } ),
    Object.assign( new Photo(), {
        'id': 56,
        'title': 'Brice Creek',
        'description': 'Brice Creek in the Umpqua National Forest, Oregon.',
        'rating': 3,
        'date': 1506019024000,
        'camera': 'Canon EOS 5D Mark II',
        'lens': 'EF24-70mm f/2.8L II USM',
        'aperture': '5.6',
        'shutterSpeed': '1',
        'iso': '50',
        'flash': 16,
        'focalLength': 31,
        'focusDistance': 3.74,
        'latitude': null,
        'longitude': null,
        'altitude': null,
        'copyright': '©2017 Nathaniel Roe',
        'usageTerms': 'All Rights Reserved',
        'isPublished': true,
        'expeditionId': 1,
        'images': [
            {
                'id': 122,
                'url': '/res/71013586-672a-4064-900b-f2f39fcc7844.jpg',
                'fileName': null,
                'width': 1365,
                'height': 2048
            },
            {
                'id': 123,
                'url': '/res/bc86edc9-ffde-4aeb-9aa7-80e2bc633c50.jpg',
                'fileName': null,
                'width': 682,
                'height': 1024
            },
            {
                'id': 124,
                'url': '/res/09d7f875-b20b-49b1-9816-8dee2182a667.jpg',
                'fileName': null,
                'width': 341,
                'height': 512
            },
            {
                'id': 125,
                'url': '/res/e123bcf2-39b9-4375-8c25-ad3080f48fd4.jpg',
                'fileName': null,
                'width': 170,
                'height': 256
            }
        ]
    } )
];
