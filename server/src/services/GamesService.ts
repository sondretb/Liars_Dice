import { createUniqueID, Result } from "../utils";
import { GameService, GameRules } from "./GameService";


type Games = Map<string, GameService>;

export interface GameID {
    id: string
}

export class GamesController {
    private games: Games;
    private static INSTANCE: GamesController;

    constructor() {
        this.games = new Map<string,GameService>();
    }

    createGame(rules: GameRules): Result<GameID> {
        const id = createUniqueID(this.games);
        this.games.set(id, new GameService(id, rules));
        return {
            data: { id }
        };
    }

    static getInstance(): GamesController {
        if(GamesController.INSTANCE === undefined) {
            GamesController.INSTANCE = new GamesController();
        }
        return GamesController.INSTANCE;
    }
}