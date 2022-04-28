import { Namespace, Server } from "socket.io";
import { GameManager } from "./gameManager";
import { LobbyPlayer } from "./lobby/player";

const MAX_PLAYERS = 5;

export class Lobby {
    private io: Server;
    private id: string;
    private nsp: Namespace;
    private players: Map<string, LobbyPlayer>;
    private gameManager: GameManager;

    constructor(io: Server, id: string) {
        this.io = io;
        this.id = id;
        this.players = new Map<string, LobbyPlayer>();
        this.gameManager = GameManager.getInstance(io);
        this.nsp = this.io.of(`/lobbies/${this.id}`);
        this.nsp.on("connect", socket => {
            console.log(socket.id, " connected")

            if (this.players.entries.length < MAX_PLAYERS) {
                this.players.set(socket.id, new LobbyPlayer(socket.id));
                this.emitState();
    
                socket.on("disconnect", () => {
                    console.log(socket.id, " disconnected")
                    this.players.delete(socket.id);
                    this.emitState();
                })
    
                socket.on("lobby:toggleReady", () => {
                    const player = this.players.get(socket.id);
                    if (player != undefined) {
                        player.toggleReady();
                        this.emitState();
                        const allPlayersReady = [...this.players.values()].every((player) => player.getReady()); 
                        if (this.players.size > 1 && allPlayersReady) {
                            const id = this.gameManager.createGame(this.players.size);
                            this.nsp.emit("lobby:ready", {id: id});
                        }
                    }
                })
            }
            else {
                socket.emit("lobby:full");
                socket.disconnect();
            }
        });
    }

    getID(): string {
        return this.id;
    }

    private emitState() {
        console.log("sending state");
        this.nsp.emit("lobby:update", {
            id: this.id,
            players: Object.fromEntries(this.players),
        })
    }

    public getPlayerCount() {
        return this.players.size;
    }

    public delete() {
        this.nsp.disconnectSockets();
        this.nsp.removeAllListeners();
        this.io._nsps.delete(`/lobbies/${this.id}`);
    }
}