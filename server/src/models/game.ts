import { count } from "console";
import { start } from "repl";
import { Namespace, Server } from "socket.io";
import { Bet } from "./game/bet";
import { GamePlayer } from "./game/player";

export class Game {
    private io: Server;
    private id: string;
    private nsp: Namespace;
    private playerCount: number;
    private players: Map<string, GamePlayer>;
    private turnList: string[];
    private currentTurn: number;

    constructor(io: Server, id: string, playerCount: number) {
        this.io = io;
        this.id = id;
        this.nsp = this.io.of(`/games/${this.id}`);
        this.players = new Map<string, GamePlayer>();
        this.turnList = [];
        this.currentTurn = 0;
        this.playerCount = playerCount;

        this.nsp.on("connect", socket => {
            console.log(`${socket.id} connected to game: ${this.id}`);
            this.players.set(socket.id, new GamePlayer(socket.id));
            this.turnList.push(socket.id);
            if (this.players.size === this.playerCount) {
                this.rollDice();
                this.emitState();
            }

            socket.on("disconnect", () => {
                console.log(socket.id , "disconnected");
                const player = this.players.get(socket.id);
                if (player !== undefined) {
                    while (player.getLives() > 0) {
                        player.removeLife()
                    }
                    this.checkForWinner();
                }
                this.emitState();
            })

            socket.on("game:bet", (args) => {
                console.log(socket.id," is trying to bet")
                const currentPlayerID = this.turnList[this.currentTurn];
                const currentPlayer = this.players.get(currentPlayerID);
                const currentPlayerBet = currentPlayer !== undefined ? currentPlayer.getLastBet() : null;
                /* if sockets turn */
                if (currentPlayer !== undefined && socket.id === currentPlayerID){
                    const {amount, value} = args;
                    // if valid bet
                    if (
                        amount !== undefined &&
                        amount > 0 &&
                        value !== undefined &&
                        value > 0 &&
                        (
                            currentPlayerBet == null ||
                            currentPlayerBet.getAmount() < amount ||
                            currentPlayerBet.getValue() < value
                        )

                    ) {
                        currentPlayer.setBet(new Bet(value, amount));
                        this.nextTurn();
                        this.emitState();
                    }

                }
            }) 

            socket.on("game:call", () => {
                console.log(socket.id," is trying to call")
                const currentPlayerID = this.turnList[this.currentTurn]
                // if sockets turn
                if (socket.id === currentPlayerID) {
                    console.log("it's ",socket.id,"'s turn")
                    const currentPlayer = this.players.get(currentPlayerID);
                    const prevPlayer = this.getPrevPlayer();
                    // if prevPlayer has made a bet
                    if (currentPlayer !== undefined && prevPlayer !== undefined) {
                        console.log("both current and prev player is defined")
                        const prevPlayerBet = prevPlayer.getLastBet()

                        console.log("prevplayer: ", prevPlayer);
                        console.log("prevplayerbet: ",prevPlayerBet);
                        if (prevPlayerBet !== null) {
                            console.log("prevplayer has a bet")
                            let countDice = 0;
                            this.turnList.forEach(playerId => {
                                const player = this.players.get(playerId);
                                if (player !== undefined){
            
                                    player.getDice().forEach(dice => {
                                        if (dice.getValue() === prevPlayerBet.getValue()) {
                                            console.log("counting: ",countDice)
                                            countDice++;
                                        }
                                    })
                                }
                            })
                            console.log("counted: ", countDice)
                            if (countDice >= prevPlayerBet.getAmount()) {
                                console.log("call failed");
                                currentPlayer.removeLife();
                                if (currentPlayer.getLives() <= 0) {
                                    this.emitLost(currentPlayer.getID());
                                    this.checkForWinner();
                                }
                            }
                            else {
                                console.log("call success");
                                prevPlayer.removeLife();
                                if(prevPlayer.getLives() <= 0) {
                                    this.emitLost(prevPlayer.getID());
                                    this.checkForWinner();
                                }

                            }
                            this.rollDice();
                            this.resetBets();
                            this.nextTurn();
                            this.emitState();
                        }
                    }
                }
            }) 
        })
    }

    private nextTurn() {
        const startTurn = this.currentTurn;
        this.currentTurn = (this.currentTurn + 1) % this.turnList.length;
        let nextPlayerID = this.turnList[this.currentTurn];
        let nextPlayer = this.players.get(nextPlayerID)
        while (nextPlayer === undefined || nextPlayer.getLives() <= 0) {

            this.currentTurn = (this.currentTurn + 1) % this.turnList.length;            if (this.currentTurn == startTurn) {
                this.emitFatalError();
                break;
            }
            nextPlayerID = this.turnList[this.currentTurn];
            nextPlayer = this.players.get(nextPlayerID);
        }
    }

    private getPrevPlayer(): GamePlayer | undefined {
        const prevPlayerID = this.turnList[(this.currentTurn - 1 + this.turnList.length) % this.turnList.length];
        return this.players.get(prevPlayerID);
    }

    private rollDice() {
        [...this.players.values()].forEach(player => {
            player.rollDice();
        });
    }

    private resetBets() {
        [...this.players.values()].forEach(player => player.setBet(null));
    }

    private emitState() {
        this.nsp.emit("game:update", {
            id: this.id,
            players: Object.fromEntries(this.players),
            turnList: this.turnList,
            currentTurn: this.currentTurn,
        })
    }

    private emitFatalError() {
        console.log("Fatal error");
        this.nsp.emit("game:error:fatal");
    }

    private emitWin(id: string) {
        const socket = this.nsp.sockets.get(id);
        if (socket !== undefined) {
            socket.emit("game:won");
        }
    }

    private emitLost(id: string) {
        const socket = this.nsp.sockets.get(id);
        if (socket !== undefined) {
            socket.emit("game:lost");
        }
    }

    private checkForWinner() {
        const remainingPlayers = [...this.players.values()].filter(player => player.getLives() > 0);
        if (remainingPlayers.length === 1) {
            this.emitWin(remainingPlayers[0].getID());
        }
    }
}