import { Component, OnInit } from '@angular/core';

import { Expedition } from '../expedition';
import { ExpeditionService } from '../expedition.service';
import { PhotoService } from '../photo.service';

@Component( {
    selector: 'app-expedition-gallery',
    templateUrl: './expedition-gallery.component.html',
    styleUrls: ['./expedition-gallery.component.css']
} )
export class ExpeditionGalleryComponent implements OnInit {
    expeditions: Expedition[];
    expeditionHighlights: Map<number, RenderedPhoto[]>;

    constructor( private expeditionService: ExpeditionService, private photoService: PhotoService ) { }

    ngOnInit(): void {
        console.log( "subscribe to expeditions" );
        this.expeditionService.getAllExpeditions().subscribe(
            data => {
                this.expeditions = data;
                this.expeditionHighlights = new Map();
                for ( let expedition of this.expeditions ) {
                    this.getHighlights( expedition.id );
                }
                this.layout();
            } );
    }

    layout(): void {
    }

    getHighlights( expeditionId: number ): void {
        this.photoService.getPhotoHighlightsByExpedition( expeditionId ).subscribe(
            data => {
                this.expeditionHighlights.set( expeditionId, data );
            }
        );
    }
}
