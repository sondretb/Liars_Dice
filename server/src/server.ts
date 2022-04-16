import {Server} from "socket.io";

const io = new Server();

/* connection is a default event when someone connects to the server */
io.on("connection", (): void => {
    console.log("Someone connected!");
})

/* CREATE_LOBBY is a custom event emited by the client */
io.on("CREATE_LOBBY", (): void => {
   console.log("Someone wants to create a lobby!");
})

/* JOIN_LOBBY is a custom event emited by the client */
io.on("JOIN_LOBBY", (): void => {
    console.log("Someone wants to join a lobby")
});

/* 3000 is the port number used by the server */
io.listen(3000);
console.log("Listening on 3000");