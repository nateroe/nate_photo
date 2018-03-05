export class Photo {
    id: number;
    title: string;
    description: string;
    rating: number;
    date: Date;
    camera: string;
    lens: string;
    aperture: string;
    shutterSpeed: string;
    iso: string;
    flash: number;
    focalLength: number;
    focusDistance: number;
    latitude: number;
    longitude: number;
    altitude: number;
    copyright: string;
    usageTerms: string;
    isPublished: boolean;
    images: ImageResource[];
}