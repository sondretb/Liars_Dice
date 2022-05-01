import { Namespace, Socket } from "socket.io";
import { LobbiesController } from "../services/LobbiesService";
import { Server } from "../server";

const EVENT = {
    CONNECT: "connect",
    CREATE_LOBBY: "lobby:create",
    JOIN_LOBBY: "lobby:join",
}

export class LobbiesAPI {
    private server: Server;
    private nsp: Namespace;
    private controller: LobbiesController;
    
    constructor() {
        this.server = Server.getInstance();
        this.nsp = this.server.createNameSpace("/lobbies");
        this.controller = LobbiesController.getInstance();
    }

    listen(): void {
        this.nsp.on(EVENT.CONNECT, (socket: Socket) => {
            socket.on(EVENT.CREATE_LOBBY, (data, callback) => {
                console.log(socket.id, " trying to create lobby")
                const result = this.controller.createLobby();
                console.log("result: ",result)
                callback(result)
            })
            
            socket.on(EVENT.JOIN_LOBBY, (data, callback) => {
                const {id} = data;
                console.log(socket.id, " trying to join lobby ",id)
                const result = this.controller.joinLobby(id)
                console.log("result: ",result)
                callback(result);
            })
        })
    }
}