import {Server} from "socket.io";

const io = new Server();

io.on("connection", (): void => {
    console.log("Someone connected!");
})

io.on("CREATE_LOBBY", (): void => {
   console.log("Someone wants to create a server!");
})

io.on("JOIN_LOBBY", (): void => {
    console.log("Someone wants to join a server")
});

io.listen(3000);
console.log("Listening on 3000");