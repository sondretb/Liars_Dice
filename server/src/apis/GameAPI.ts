import { Namespace } from "socket.io";
import { GameService, GameState } from "../services/GameService";
import { Bet } from "../models/game/bet";
import { Server } from "../server";
import { Result } from "../utils";



export class GameAPI {
    static EVENT = {
        BET: "game:action:bet",
        CALL: "game:action:call",
        DISCONNECT: "disconnect"
    }
    private server: Server;
    private id: string;
    private nsp: Namespace;
    private controller: GameService;

    constructor(id: string, controller: GameService) {
        this.server = Server.getInstance();
        this.id = id;
        this.nsp = this.server.createNameSpace(`games/${this.id}`);
        this.controller = controller;
    }

    listen() {
        this.nsp.on("connect", socket => {
            console.log(socket.id, "joined the game ", this.id);
            this.controller.addPlayer(socket.id);
            socket.on("disconnect", ()=> {
                // TODO: Disconnect stuff
            })

            socket.on("game:action:bet", args => {
                console.log(socket.id, " wants to make a bet")
                const {amount,value} = args;
                if (amount === undefined || value === undefined) {
                    return;
                }
                const {data,error} = this.controller.makeBet(socket.id, new Bet(value, amount));
                if (error) {
                    return;
                }
                if (data === undefined) {
                    return;
                }
                const {data: state} = this.controller.makeBet(socket.id, new Bet(value,amount));
                if (state !== undefined) {
                    this.updateState(state);

                }
            }) 

            socket.on("game:action:call" , () => {
                console.log(socket.id, " wants to call")
                const {data, error} = this.controller.call(socket.id);
                if (error !== undefined) {
                    return;
                }
                if (data === undefined) {
                    return;
                }
            })
        })
    }

    playerWins(id: string) {
        console.log("gameAPI running playerWins")
        const socket = this.nsp.sockets.get(id);
        if (socket !== undefined) {
            socket.emit("game:won");
        }
    }

    playerLost(id: string) {
        console.log("gameAPI running playerLost")
        const socket = this.nsp.sockets.get(id);
        if (socket !== undefined) {
            socket.emit("game:lost");
        }
    }

    updateState(data: GameState) {
        console.log("sending game state ", data)
        const result = {data}
        this.nsp.emit("game:state:update", result);
    }

    deleteNameSpace() {
        this.server.deleteNameSpace(this.nsp);
    }
}