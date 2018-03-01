import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';


import { AppComponent } from './app.component';
import { PhotoComponent } from './photo/photo.component';
import { PhotoService } from './photo.service';


@NgModule({
  declarations: [
    AppComponent,
    PhotoComponent
  ],
  imports: [
    BrowserModule
  ],
  providers: [PhotoService],
  bootstrap: [AppComponent]
})
export class AppModule { }
