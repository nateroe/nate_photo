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
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PhotoDetailComponent } from './components/photo-detail/photo-detail.component';
import { PhotoGalleryComponent } from './components/photo-gallery/photo-gallery.component';
import { ExpeditionDetailComponent } from './components/expedition-detail/expedition-detail.component';
import { ExpeditionGalleryComponent } from './components/expedition-gallery/expedition-gallery.component';
import { BestOfGalleryComponent } from './components/best-of-gallery/best-of-gallery.component';

const routes: Routes = [
    { path: '', component: BestOfGalleryComponent },
    { path: 'photo/:photoId', component: PhotoDetailComponent },
    { path: 'expeditions', component: ExpeditionGalleryComponent },
    { path: 'expedition/:expeditionId', component: ExpeditionDetailComponent }
];

@NgModule( {
    imports: [RouterModule.forRoot( routes )],
    exports: [RouterModule]
} )
export class AppRoutingModule { }
