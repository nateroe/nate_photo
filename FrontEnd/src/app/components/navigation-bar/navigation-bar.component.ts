import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component( {
    selector: 'app-navigation-bar',
    templateUrl: './navigation-bar.component.html',
    styleUrls: ['./navigation-bar.component.css']
} )
export class NavigationBarComponent implements OnInit {

    constructor( private router: Router ) { }

    ngOnInit() {
        console.log( "this.router.url: " + this.router.url + " isOnHomePage: " + this.isOnHomePage() );
    }

    isOnHomePage(): boolean {
        return this.router.url === "/";
    }
}
