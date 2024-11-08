import {WorkCountdown} from "./WorkCountdown.tsx";

export interface WorkSummaryResponse {
    workCountdowns: WorkCountdown[];
    totalByType: {[key: string]: number};
    grandTotal: number;
}