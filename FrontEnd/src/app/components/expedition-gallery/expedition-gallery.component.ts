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
import { Component, OnInit } from '@angular/core';

import { Expedition } from '../../model/expedition';
import { RenderedPhoto } from '../../model/rendered-photo';
import { ExpeditionService } from '../../services/expedition.service';
import { PhotoService } from '../../services/photo.service';

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
