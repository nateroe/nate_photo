import { Component, Input, OnInit } from '@angular/core';

@Component( {
    selector: 'app-site-link',
    templateUrl: './site-link.component.html',
    styleUrls: ['./site-link.component.css']
} )
export class SiteLinkComponent implements OnInit {
    @Input()
    isImageFirst: boolean = false;

    @Input()
    linkDestination: string;

    @Input()
    linkText: string;

    @Input()
    imgSrc: string;

    @Input()
    imgSrcHover: string;

    isMouseOver: boolean = false;

    constructor() { }

    ngOnInit() {
    }

    mouseEnter(): void {
        this.isMouseOver = true;
    }

    mouseLeave(): void {
        this.isMouseOver = false;
    }

    /**
     * Reassemble link text (title) from individual words
     * 
     * @param words
     * @param firstIndex
     * @param lastIndex
     */
    assembleTitle( words: string[], firstIndex, lastIndex ) {
        let result: string = null;
        for ( let i: number = firstIndex; i <= lastIndex; i++ ) {
            if ( i == firstIndex ) {
                result = words[i];
            } else {
                result += " " + words[i];
            }
        }
        return result;
    }

    /**
     * Return all but the last word of the expedition title
     */
    getTitleFirstPart( title: string ): string {
        let words: string[] = title.split( ' ' );
        return this.assembleTitle( words, 0, words.length - 2 );
    }

    /**
     * Return all but the first word of the expedition title
     */
    getTitleLastPart( title: string ): string {
        let words: string[] = title.split( ' ' );
        console.log( "last part: " + this.assembleTitle( words, 1, words.length - 1 ) );
        return this.assembleTitle( words, 1, words.length - 1 );
    }

    getTitleLastWord( title: string ): string {
        let words: string[] = title.split( ' ' );
        return words[words.length - 1];
    }

    getTitleFirstWord( title: string ): string {
        return title.split( ' ' )[0];
    }

    getImageSrc(): string {
        console.log( "imgSrc: " + ( this.isMouseOver ) ? this.imgSrcHover : this.imgSrc );
        return ( this.isMouseOver ) ? this.imgSrcHover : this.imgSrc;
    }
}
