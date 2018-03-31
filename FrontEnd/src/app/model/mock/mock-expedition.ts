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
import { Expedition } from '../expedition';

export const EXPEDITION: Expedition = Object.assign( new Expedition(), {
    'id': 1,
    'title': 'Brice Creek',
    'description': 'A creek-side hike along Brice Creek in the Row River watershed.',
    'beginDate': new Date( 1506013145000 ),
    'endDate': new Date( 1506021068000 )
} );

export const EXPEDITIONS: Expedition[] = [
    Object.assign( new Expedition(),
        {
            'id': 1,
            'title': 'Brice Creek',
            'description': 'A creek-side hike along Brice Creek in the Row River watershed.',
            'beginDate': new Date( 1506013145000 ),
            'endDate': new Date( 1506021068000 )
        } ),
    Object.assign( new Expedition(),
        {
            'id': 2,
            'title': 'Olympic National Park',
            'description': 'This trip was a few days at Second and Third Beach, south of La Push, Washington.',
            'beginDate': new Date( 1504764831000 ),
            'endDate': new Date( 1504853553000 )
        } ),
    Object.assign( new Expedition(),
        {
            'id': 3,
            'title': 'Zion National Park',
            'description': '',
            'beginDate': new Date( 1488649641000 ),
            'endDate': new Date( 1488731345000 )
        } )];
