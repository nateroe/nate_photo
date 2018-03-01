import { Component, OnInit } from '@angular/core';
import { Photo } from '../photo';

@Component( {
    selector: 'app-photo',
    templateUrl: './photo.component.html',
    styleUrls: ['./photo.component.css']
} )
export class PhotoComponent implements OnInit {

    width: number = 500;
    
    // eventually this will come from the back end
    photo: Photo = Object.assign( new Photo(), {
        "id": 1,
        "title": "Sunset on the Olympic Coast",
        "description": "Sunset on the Olympic Coast. Olympic National Park, Washington.",
        "rating": 5,
        "date": new Date( 1503026212000 ),
        "camera": "Canon EOS 5D Mark II",
        "lens": "EF24-70mm f/2.8L II USM",
        "aperture": "22",
        "shutterSpeed": "2.0",
        "iso": "ISO 50",
        "isFlashFired": false,
        "focusDistance": 300,
        "copyright": "©2017 Nathaniel Roe",
        "isMakingOf": false,
        "isPublished": true,
        "images": [
            {
                "id": 1,
                "url": "http://phlake.org/natephoto/IMG_6711-2-Edit-1.jpg",
                "width": 5510,
                "height": 3645
            },
            {
                "id": 2,
                "url": "http://phlake.org/natephoto/IMG_6711-2-Edit-2.jpg",
                "width": 2048,
                "height": 1355
            },
            {
                "id": 3,
                "url": "http://phlake.org/natephoto/IMG_6711-2-Edit-3.jpg",
                "width": 1024,
                "height": 677
            },
            {
                "id": 4,
                "url": "http://phlake.org/natephoto/IMG_6711-2-Edit-4.jpg",
                "width": 512,
                "height": 339
            },
            {
                "id": 5,
                "url": "http://phlake.org/natephoto/IMG_6711-2-Edit-5.jpg",
                "width": 256,
                "height": 169
            },
            {
                "id": 6,
                "url": "http://phlake.org/natephoto/IMG_6711-2-Edit-6.jpg",
                "width": 128,
                "height": 85
            }
        ]
    } );

    bestResource: ImageResource;

    ngOnInit() {
        this.bestResource = this.photo.getBestResource(this.width, this.width);
    }
}
