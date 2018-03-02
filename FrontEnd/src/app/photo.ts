export class Photo {
    id: number;
    title: string;
    description: string;
    rating: number;
    date: Date;
    camera: string;
    lens: string;
    aperture: string;
    iso: string;
    shutterSpeed: string;
    isFlashFired: boolean;
    focusDistance: number;
    copyright: string;
    isMakingOf: boolean;
    isPublished: boolean;
    images: ImageResource[];
}