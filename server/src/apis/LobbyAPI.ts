import { Namespace, Socket } from "socket.io";
import { GameID } from "../services/GamesService";
import { LobbyController, LobbyState } from "../services/LobbyService";
import { Server } from "../server";
import { Result } from "../utils";

const EVENT = {
    CONNECT: "connect",
    DISCONNECT: "disconnect",
    TOGGLE_READY: "lobby:ready:toggle",
    ERROR: "lobby:error"
}

export class LobbyAPI {
    private server: Server;
    private id: string;
    private nsp: Namespace;
    private controller: LobbyController;

    constructor(id: string, controller: LobbyController) {
        this.server = Server.getInstance();
        this.id = id;
        this.nsp = this.server.createNameSpace(`lobbies/${this.id}`);
        this.controller = controller;
    }

    listen() {
        this.nsp.on(EVENT.CONNECT, (socket: Socket) => {
            this.controller.addPlayer(socket.id);
            socket.on(EVENT.DISCONNECT, () => {
                this.controller.removePlayer(socket.id);
            })

            socket.on(EVENT.TOGGLE_READY, () => {
                // toggle player's ready status
                this.controller.toggleReady(socket.id);
            })
        })
    }


    startGame(data: GameID) {
        const result = {data}
        this.nsp.emit("lobby:game:start", result);
    }

    updateState(data: LobbyState) {
        const result: Result<LobbyState> = {data};
        this.nsp.emit("lobby:state:update", result)
    }
}