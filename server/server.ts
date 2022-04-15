const { Server } = require("socket.io");

const io = new Server();

io.on("connection", () => {
    console.log("Someone connected!")
})

io.listen(3000);
console.log("Listening on 3000")