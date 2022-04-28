import {Server} from "socket.io";
import { LobbyManager } from "./models/lobbyManager";
import { createID } from "./utils";

const io = new Server();

// eslint-disable-next-line @typescript-eslint/no-unused-vars
const lobbyManager = new LobbyManager(io);

/* 3000 is the port number used by the server */
io.listen(3000);
console.log("Listening on 3000");