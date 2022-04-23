import { Server } from "socket.io";
import { createID } from "../utils";
import { Lobby } from "./lobby";

type Lobbies = Map<string, Lobby>

export class LobbyManager {
    private io: Server;
    private lobbies: Lobbies;

    constructor(io: Server) {
        this.io = io;
        this.lobbies = new Map<string, Lobby>();

        const lobbiesNSP = this.io.of("/lobbies");

        lobbiesNSP.on("connect", socket => {
            console.log("somebody connected to the lobbyManager")
            socket.on("disconnect", () => {
                console.log(`${socket.id} disconnected`);
            })
            socket.on("lobby:create", (args, ack) => {
                console.log("somebody wants to create a lobby")
                let id = createID();
                while (this.lobbies.has(id)) {
                    id = createID();
                }
                this.lobbies.set(id, new Lobby(this.io, id));
                console.log("lobby created with id "+id);
                ack({
                    success: true,
                    data: {
                        id: id
                    }
                })
            })
            socket.on("lobby:join", (args, ack) => {
                console.log("somebody wants to join a lobby")
                const {lobbyID} = args;
                if (this.lobbies.has(lobbyID)) {
                    ack({
                        success: true,
                        data: {}
                    })
                }
                else {
                    ack({
                        success: false,
                        data: {
                            error: "No lobby with that ID"
                        }
                    })
                }
            })
        })
    }
}