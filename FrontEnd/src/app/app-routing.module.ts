import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PhotoDetailComponent } from './photo-detail/photo-detail.component';
import { PhotoCollectionComponent } from './photo-collection/photo-collection.component';

const routes: Routes = [
    { path: '', component: PhotoCollectionComponent },
    { path: 'photo/:photoId', component: PhotoDetailComponent }
];

@NgModule( {
    imports: [RouterModule.forRoot( routes )],
    exports: [RouterModule]
} )
export class AppRoutingModule { }
