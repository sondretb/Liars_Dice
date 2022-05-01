import { GameAPI } from "../apis/GameAPI";
import { Bet } from "../models/game/bet";
import { GamePlayer } from "../models/game/player";
import { Result } from "../utils";

export interface GameState {
    id: string,
    players: { [k: string]: GamePlayer},
    turnList: string[],
    currentTurn: number
}


export interface GameRules {
    players: number
}

export class GameService {
    private id: string;
    private api: GameAPI
    private players: Map<string, GamePlayer>;
    private rules: GameRules;
    private turnList: string[];
    private currentTurn: number;

    constructor(id: string, rules: GameRules) {
        this.id = id;
        this.api = new GameAPI(id, this);
        this.players = new Map<string, GamePlayer>();
        this.rules = rules;
        this.turnList = [];
        this.currentTurn = 0;
        this.api.listen()
    }

    call(id: string): Result<GameState> {
        console.log(id)
        const currentPlayer = this.getCurrentPlayer();
        if (currentPlayer === undefined) {
            console.log("No current player")
            return {
                error: "No current player"
            }
        } 
        if (!(currentPlayer.getID() === id)) {
            console.log("Not current player")
            return {
                error: "Not players turn"
            }
        }

        const prevPlayer = this.getPrevPlayer();
        if (prevPlayer === undefined) {
            console.log("No previous player")
            return {
                error: "No previous player"
            }
        }

        const prevPlayerBet = prevPlayer.getLastBet();
        if (prevPlayerBet === null) {
            console.log("Previous player has made no bet")
            return {
                error: "Previous player has made no bet"
            }
        }

        if (this.checkBet(prevPlayerBet)) {
            console.log("remove life from currentPlayer")
            currentPlayer.removeLife();
            if (currentPlayer.getLives() <= 0) {
                this.turnList.splice(this.turnList.indexOf(currentPlayer.getID()));
                this.api.playerLost(currentPlayer.getID());
                if (this.turnList.length <= 1) {
                    this.api.playerWins(prevPlayer.getID())
                    return {};
                }
            }
        }
        else {
            console.log("remove life from prev player")
            prevPlayer.removeLife();
            if (prevPlayer.getLives() <= 0) {
                this.turnList.splice(this.turnList.indexOf(prevPlayer.getID()));
                this.api.playerLost(prevPlayer.getID());
                if (this.turnList.length <= 1) {
                    this.api.playerWins(currentPlayer.getID())
                    return {};
                }
            }
        }
        console.log("getting ready for next turn");
        this.rollDice();
        this.resetBets();
        this.nextTurn();
        const state = this.getGameState();
        this.api.updateState(state);
        return {}
    }

    makeBet(id: string, bet: Bet): Result<GameState> {
        console.log(id, "making bet");
        if (!this.isPlayersTurn(id)) {
            return {
                error: "Not players turn"
            }
        }
        if (!this.isValidBet(bet)) {
            return {
                error: "Invalid bet"
            }
        }
        const player = this.players.get(id);
        if (player !== undefined) {
            player.setBet(bet);
            this.nextTurn()
            const state = this.getGameState();
            this.api.updateState(state);
            return {}
        }
        return {
            error: "Unknown error"
        }
    }

    checkBet(bet: Bet): boolean {
        let diceCount = 0;
        this.turnList.forEach(id => {
            const player = this.players.get(id);
            if (player !== undefined) {
                player.getDice().forEach(dice => {
                    diceCount += dice.getValue() === bet.getValue() ? 1 : 0;
                })
            }
        });
        return diceCount >= bet.getAmount();
    }

    resetBets() {
        this.players.forEach(player => player.setBet(null));
    }

    rollDice() {
        this.players.forEach(player => player.rollDice());
    }

    nextTurn() {
        this.currentTurn = (this.currentTurn + 1) % this.turnList.length;
    }

    getPrevPlayer(): GamePlayer | undefined {
        const prevTurn = (this.currentTurn + this.turnList.length - 1) % this.turnList.length;
        const prevPlayerID = this.turnList[prevTurn];
        return this.players.get(prevPlayerID);
    }

    getCurrentPlayer(): GamePlayer | undefined {
        const currentPlayerID = this.turnList[this.currentTurn];
        return this.players.get(currentPlayerID);
    }

    isPlayersTurn(id: string): boolean {
        return id === this.turnList[this.currentTurn];
    }

    isValidBet(bet: Bet) {
        const playerID = this.turnList[this.currentTurn];
        const player = this.players.get(playerID);

        if (player === undefined) return false;

        const lastBet = player.getLastBet();
        
        if (lastBet === null) return true;
        if (lastBet.getAmount() < bet.getAmount()) return true;
        if (lastBet.getValue() < bet.getValue()) return true;
        return false;
    }

    hasPrevPlayerBet() {
        const prevPlayer = this.getPrevPlayer();
        if (prevPlayer === undefined || prevPlayer.getLastBet() === null) {
            return false;
        }
        return true;
    }

    getGameState(): GameState {
        return {
            id: this.id,
            players: Object.fromEntries(this.players),
            turnList: this.turnList,
            currentTurn: this.currentTurn,
        }
    }

    addPlayer(id: string) {
        this.players.set(id, new GamePlayer(id));
        this.turnList.push(id);
        console.log("rules players", this.rules.players);
        if (this.players.size === this.rules.players) {
            console.log("rolling dice")
            this.rollDice();
            const state = this.getGameState()
            this.api.updateState(state);
        }
    }
}