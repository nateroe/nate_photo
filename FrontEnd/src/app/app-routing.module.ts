import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PhotoComponent } from './photo/photo.component';

const routes: Routes = [
    { path: '', redirectTo: '/photo/1', pathMatch: 'full' },
    { path: 'photo/:photoId', component: PhotoComponent }
];

@NgModule( {
    imports: [RouterModule.forRoot( routes )],
    exports: [RouterModule]
} )
export class AppRoutingModule { }
