import { LobbyAPI } from "../apis/LobbyAPI";
import { LobbyPlayer } from "../models/lobby/player";
import { Result } from "../utils";
import { GameRules } from "./GameService";
import { GameID, GamesController } from "./GamesService";

export interface LobbyState {
    id: string;
    players: {
        [key: string]: LobbyPlayer
    };
}


export class LobbyController {
    private id: string
    private api: LobbyAPI;
    private rules: GameRules;
    private gamesController: GamesController;
    private players: Map<string, LobbyPlayer>;

    constructor(id: string) {
        this.id = id;
        this.api = new LobbyAPI(this.id, this);
        this.rules = {
            players: 5,
        }
        this.gamesController = GamesController.getInstance();
        this.players = new Map<string,LobbyPlayer>();
        this.api.listen()
    }

    addPlayer(playerID: string): Result {
        if (this.players.has(playerID)) {
            return {error: "ID already joined"};
        }
        if (this.players.size >= this.rules.players) {
            return {error: "Lobby already full"};
        }
        this.players.set(playerID, new LobbyPlayer(playerID));
        const state = this.getState();
        this.api.updateState(state);
        return {};
    }

    removePlayer(playerID: string): Result {
        if (!this.players.has(playerID)) {
            return {error: "No player with that ID"}
        }
        this.players.delete(playerID);
        return {};
    }

    toggleReady(playerID: string): Result {
        const player = this.players.get(playerID);
        if (player === undefined) {
            return {error: "No player with that ID"}
        }
        player.toggleReady();
        const state = this.getState();
        this.api.updateState(state);
        if (this.isAllReady()) {
            const {data, error} = this.createGame()
            if (error) {
                return {error};
            }
            if (data !== undefined) {
                this.api.startGame(data);
            }
        }
        return {}
    }

    private createGame(): Result<GameID> {
        if (!this.isAllReady()) {
            return {error: "Not all players are ready"}; // Not all players are ready
        }
        const {data} = this.gamesController.createGame({players: this.players.size})
        if (data === undefined) {
            return {error: "Unable to create game"}; // Unable to create game
        } 
        return {data};
    }

    private getState(): LobbyState {
        return {
            id: this.id,
            players: Object.fromEntries(this.players)
        }
    }

    private isAllReady(): boolean {
        const players = [...this.players.values()]
        if (players.every(player => player.getReady())) {
            return true;
        }
        return false;
    }
}