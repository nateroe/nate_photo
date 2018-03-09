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
