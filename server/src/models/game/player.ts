import { Bet } from "./bet";
import { D6, Dice } from "./dice";

export class GamePlayer {
    private id: string;
    private dice: Dice[];
    private lastBet: Bet | null;

    constructor(id: string) {
        const lives = 5;
        this.id = id;
        this.dice = [];
        for (let i = 0; i < lives; i++) {
            this.dice.push(new D6());
        }
        this.lastBet = null;
    }

    getLives() {
        return this.dice.length;
    }

    removeLife() {
        this.dice.pop()
    }

    getDice() {
        return this.dice;
    }

    rollDice() {
        this.dice.forEach(dice => dice.roll());
    }

    getLastBet(): Bet | null {
        return this.lastBet;
    }

    setBet(bet: Bet | null) {
        this.lastBet = bet;
    }

    getID(): string {
        return this.id;
    }
}