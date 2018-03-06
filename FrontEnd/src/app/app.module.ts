import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';


import { AppComponent } from './app.component';
import { PhotoDetailComponent } from './photo-detail/photo-detail.component';
import { PhotoService } from './photo.service';
import { AppRoutingModule } from './/app-routing.module';
import { PhotoCollectionComponent } from './photo-collection/photo-collection.component';


@NgModule( {
    declarations: [
        AppComponent,
        PhotoDetailComponent,
        PhotoCollectionComponent
    ],
    imports: [
        BrowserModule,
        HttpClientModule,
        AppRoutingModule
    ],
    providers: [PhotoService],
    bootstrap: [AppComponent]
} )
export class AppModule { }
