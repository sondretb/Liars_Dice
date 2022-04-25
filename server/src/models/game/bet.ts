export class Bet {
    private value: number; // value on dice
    private amount: number; // number of dies with that value

    constructor(value: number, amount: number) {
        this.value = value;
        this.amount = amount
    }

    public getValue() {
        return this.value;
    }

    public getAmount() {
        return this.amount;
    }
}