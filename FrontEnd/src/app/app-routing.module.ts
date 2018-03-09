import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PhotoDetailComponent } from './components/photo-detail/photo-detail.component';
import { PhotoGalleryComponent } from './components/photo-gallery/photo-gallery.component';
import { ExpeditionDetailComponent } from './components/expedition-detail/expedition-detail.component';
import { ExpeditionGalleryComponent } from './components/expedition-gallery/expedition-gallery.component';

const routes: Routes = [
    { path: '', component: ExpeditionGalleryComponent },
    { path: 'photo/:photoId', component: PhotoDetailComponent }
];

@NgModule( {
    imports: [RouterModule.forRoot( routes )],
    exports: [RouterModule]
} )
export class AppRoutingModule { }
