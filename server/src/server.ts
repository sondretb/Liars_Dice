import {Server} from "socket.io";
import { LobbyManager } from "./models/lobbyManager";
import { createID } from "./utils";

const io = new Server();

const lobbyManager = new LobbyManager(io);
/*
io.on("connect", (socket): void => {
    socket.on("disconnect", () => {
        console.log("someone disconnected");
    })

    socket.on("room:create", (args, ack): void => {
        console.log(`${socket.id} wants to create a room`);
        // TODO: create room
        let roomID = createID();
        while (io.of("/").adapter.rooms.has(roomID)) {
            roomID = createID()
        }
        socket.join(roomID);
        ack({
            success: true,
            message: roomID
        })
        console.log(`${socket.id} created room ${roomID}`);
    })

    socket.on("room:join", (args, ack): void => {
        const {roomID} = args;
        console.log(`${socket.id} wants to join room ${roomID}`);
        if (io.of("/").adapter.rooms.has(roomID)) {
            socket.join(roomID)
            ack({
                success: true,
                message: ""
            })
            console.log(`${socket.id} joined room ${roomID}`);
            io.to(roomID).emit("room:state")
        }
        else {
            console.log(`no room ${roomID}`);
            ack({
                success: false,
                message: `No room with ${roomID}`
            })
        }
    });

    socket.on("room:state", (args, ack): void => {
        const {roomID} = args;
        const players = io.sockets.adapter.rooms.get(roomID)?.values();
        if (players !== undefined) {
            ack({
                success: true,
                state: {
                    roomID: roomID,
                    players: Array.from(players)
                }
            })
        }
    })
})

*/


/* 3000 is the port number used by the server */
io.listen(3000);
console.log("Listening on 3000");