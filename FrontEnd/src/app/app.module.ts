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
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';


import { AppComponent } from './app.component';
import { PhotoDetailComponent } from './components/photo-detail/photo-detail.component';
import { PhotoService } from './services/photo.service';
import { ExpeditionService } from './services/expedition.service';
import { AppRoutingModule } from './/app-routing.module';
import { PhotoGalleryComponent } from './components/photo-gallery/photo-gallery.component';
import { ExpeditionDetailComponent } from './components/expedition-detail/expedition-detail.component';
import { ExpeditionGalleryComponent } from './components/expedition-gallery/expedition-gallery.component';
import { BestOfGalleryComponent } from './components/best-of-gallery/best-of-gallery.component';
import { NavigationBarComponent } from './components/navigation-bar/navigation-bar.component';
import { SiteLinkComponent } from './components/site-link/site-link.component';
import { ExpeditionHeaderComponent } from './components/expedition-header/expedition-header.component';
import { ThumbnailComponent } from './components/thumbnail/thumbnail.component';
import { ZoomViewComponent } from './components/zoom-view/zoom-view.component';

@NgModule( {
    declarations: [
        AppComponent,
        PhotoDetailComponent,
        PhotoGalleryComponent,
        ExpeditionDetailComponent,
        ExpeditionGalleryComponent,
        BestOfGalleryComponent,
        NavigationBarComponent,
        SiteLinkComponent,
        ExpeditionHeaderComponent,
        ThumbnailComponent,
        ZoomViewComponent
    ],
    imports: [
        BrowserModule,
        HttpClientModule,
        AppRoutingModule
    ],
    providers: [
        PhotoService,
        ExpeditionService
    ],
    bootstrap: [AppComponent]
} )
export class AppModule { }
