import { Photo } from './photo';

export class Expedition {
    id: number;
    name: string;
    description: string;
    beginDate: Date;
    endDate: Date;

    copyFrom( that: Expedition ): Expedition {
        Object.assign( this, that );
        this.beginDate = new Date( that.beginDate );
        this.endDate = new Date( that.endDate );
        return this;
    }

    isMultiDay(): boolean {
        let result: boolean = false;
        if ( this.beginDate != null && this.endDate != null ) {
            result = this.beginDate.getDate() != this.endDate.getDate()
                || this.beginDate.getMonth() != this.endDate.getMonth()
                || this.beginDate.getFullYear() != this.endDate.getFullYear();
        }
        return result;
    }
}
