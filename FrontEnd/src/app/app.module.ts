import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';


import { AppComponent } from './app.component';
import { PhotoDetailComponent } from './photo-detail/photo-detail.component';
import { PhotoService } from './photo.service';
import { ExpeditionService } from './expedition.service';
import { AppRoutingModule } from './/app-routing.module';
import { PhotoGalleryComponent } from './photo-gallery/photo-gallery.component';
import { ExpeditionDetailComponent } from './expedition-detail/expedition-detail.component';
import { ExpeditionGalleryComponent } from './expedition-gallery/expedition-gallery.component';


@NgModule( {
    declarations: [
        AppComponent,
        PhotoDetailComponent,
        PhotoGalleryComponent,
        ExpeditionDetailComponent,
        ExpeditionGalleryComponent
    ],
    imports: [
        BrowserModule,
        HttpClientModule,
        AppRoutingModule
    ],
    providers: [
PhotoService
ExpeditionService
],
    bootstrap: [AppComponent]
} )
export class AppModule { }
