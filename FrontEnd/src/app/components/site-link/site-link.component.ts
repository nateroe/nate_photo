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
import { Component, Input, OnInit } from '@angular/core';

/**
 * A link on the site, integrating an icon. Special word-wrapping rules so
 * that the icon wraps with the first or last word, depending if isImageFirst.
 */
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
    svgId: string;

    constructor() { }

    ngOnInit() {
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
            if ( i === firstIndex ) {
                result = words[i];
            } else {
                result += ' ' + words[i];
            }
        }
        return result;
    }

    /**
     * Return all but the last word of the expedition title
     */
    getTitleFirstPart( title: string ): string {
        const words: string[] = title.split( ' ' );
        return this.assembleTitle( words, 0, words.length - 2 );
    }

    /**
     * Return all but the first word of the expedition title
     */
    getTitleLastPart( title: string ): string {
        const words: string[] = title.split( ' ' );
        return this.assembleTitle( words, 1, words.length - 1 );
    }

    getTitleLastWord( title: string ): string {
        const words: string[] = title.split( ' ' );
        return words[words.length - 1];
    }

    getTitleFirstWord( title: string ): string {
        return title.split( ' ' )[0];
    }
}
