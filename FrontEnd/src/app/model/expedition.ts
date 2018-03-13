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

export class Expedition {
    id: number;
    title: string;
    description: string;
    beginDate: Date;
    endDate: Date;

    copyFrom( that: Expedition ): Expedition {
        Object.assign( this, that );
        this.beginDate = new Date( that.beginDate );
        this.endDate = new Date( that.endDate );
        return this;
    }

    isMultiDay(): boolean {
        let result: boolean = false;
        if ( this.beginDate != null && this.endDate != null ) {
            result = this.beginDate.getDate() != this.endDate.getDate()
                || this.beginDate.getMonth() != this.endDate.getMonth()
                || this.beginDate.getFullYear() != this.endDate.getFullYear();
        }
        return result;
    }
}
