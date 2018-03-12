import { Component, Input, OnInit } from '@angular/core';
import { Expedition } from '../../model/expedition';

@Component( {
    selector: 'app-expedition-header',
    templateUrl: './expedition-header.component.html',
    styleUrls: ['./expedition-header.component.css']
} )
export class ExpeditionHeaderComponent implements OnInit {
    @Input()
    expedition: Expedition;

    @Input()
    isLink: boolean = true;

    constructor() { }

    ngOnInit() {
    }
}
