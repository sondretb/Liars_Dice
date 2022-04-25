
export interface Dice {
    getName: () => string,
    getMax: () => number,
    getValue: () => number | null,
    roll: () => number,
}

export class D6 implements Dice {
    private name: string;
    private max: number;
    private value: number | null;

    constructor() {
        this.name = "D6";
        this.max = 6;
        this.value = null;
    }

    getName() {
        return this.name;
    }

    getMax() {
        return this.max;
    }

    getValue() {
        return this.value;
    }

    roll(): number {
        this.value = Math.floor(Math.random() * this.max) + 1; 
        return this.value;
    }
}