# nate_photo
A web app to showcase a single photographer's work. See a live demo of branch 'master' at [nateroe.com](http://nateroe.com/)

This is an Angular 5/Java EE 7 application designed for deployment in AWS or on a standalone server.

A photographer imports photos to his website by copying them to an import directory. The server polls the directory and imports newly-found photos. Upon finding new photos they are imported by the server. This consists of generating a series of mipmaps in successively smaller resolutions, reading the relevant EXIF data, writing entries to the database, and deploying the resulting images to either a local directory or Amazon S3.

Visitors to the photographer's website use the Angular application which consumes the server's RESTful services. Photos are organized into several galleries automatically based on data created during import. The application uses a responsive layout to present the photos appropriately on a variety of devices. Device screen density ("pixel ratio") is taken into account when choosing the appropriate image resource to display (high density devices request higher resolution mipmaps). 

A more detailed description of both client and server is found [in the wiki](https://github.com/nateroe/nate_photo/wiki).