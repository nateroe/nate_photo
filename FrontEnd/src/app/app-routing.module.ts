import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PhotoDetailComponent } from './photo-detail/photo-detail.component';
import { PhotoGalleryComponent } from './photo-gallery/photo-gallery.component';
import { ExpeditionDetailComponent } from './expedition-detail/expedition-detail.component';
import { ExpeditionGalleryComponent } from './expedition-gallery/expedition-gallery.component';

const routes: Routes = [
    { path: '', component: ExpeditionGalleryComponent },
    { path: 'photo/:photoId', component: PhotoDetailComponent }
];

@NgModule( {
    imports: [RouterModule.forRoot( routes )],
    exports: [RouterModule]
} )
export class AppRoutingModule { }
