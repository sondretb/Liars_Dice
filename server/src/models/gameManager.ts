import { randomUUID } from "crypto";
import { Server } from "socket.io";
import { Game } from "./game";

type Games = Map<string, Game>

export class GameManager {
    private static INSTANCE: GameManager | null = null;
    private io: Server;
    private games: Games;

    constructor(io: Server) {
        this.io = io;
        this.games = new Map<string, Game>();
    }

    static getInstance(io: Server): GameManager{
        if (this.INSTANCE != null) {
            return this.INSTANCE;
        }
        this.INSTANCE = new GameManager(io)
        return this.INSTANCE;
    }

    createGame(playerCount: number): string {
        let id = randomUUID();
        while (this.games.has(id)) {
            id = randomUUID();
        }
        this.games.set(id, new Game(this.io, id, playerCount));
        return id;
    }
}