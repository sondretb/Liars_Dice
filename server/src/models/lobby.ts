import { Namespace, Server, Socket } from "socket.io";
import { LobbyPlayer } from "./lobbyPlayer";

const MAX_PLAYERS = 5;

export class Lobby {
    private io: Server;
    private id: string;
    private nsp: Namespace;
    private players: Map<string, LobbyPlayer>;

    constructor(io: Server, id: string) {
        this.io = io;
        this.id = id;
        this.players = new Map<string, LobbyPlayer>();
        this.nsp = this.io.of(`/lobbies/${this.id}`);
        this.nsp.on("connect", socket => {
            console.log(socket.id + " connected to " + this.id)
            console.log("lobbyID: ", this.id);
            console.log("lobbyNSP")
            if (this.players.entries.length < MAX_PLAYERS) {
                console.log("BEFORE: "+[...this.players.keys()])
                this.players.set(socket.id, new LobbyPlayer(socket.id));
                console.log("AFTER: "+[...this.players.keys()])
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
        const lobbyNSP = this.io.of(`/lobbies/${this.id}`)
        lobbyNSP.emit("lobby:update", {
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