import {Namespace, Server as SocketIOServer} from "socket.io";
import { LobbiesAPI } from "./apis/LobbiesAPI";

export class Server {
    private static INSTANCE: Server;
    private io: SocketIOServer;

    private constructor() {
        this.io = new SocketIOServer();
    }

    createNameSpace(name: string) {
        return this.io.of(name);
    }

    deleteNameSpace(nsp: Namespace) {
        nsp.disconnectSockets();
        nsp.removeAllListeners();
        this.io._nsps.delete(nsp.name);
    }

    listen(port: number) {
        this.io.listen(port);
    }

    static getInstance(): Server {
        if (Server.INSTANCE === undefined) {
            Server.INSTANCE = new Server();
        }
        return Server.INSTANCE;
    }
}

const server = Server.getInstance();
const lobbiesAPI = new LobbiesAPI();

server.listen(3000);
lobbiesAPI.listen();

console.log("listening on 3000")
