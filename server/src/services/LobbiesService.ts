import { createUniqueID, Result } from "../utils";
import { LobbyController } from "./LobbyService";

type Lobbies = Map<string, LobbyController>;

interface LobbyID {
    id: string;
}

export class LobbiesController {
    private lobbies: Lobbies;
    private static INSTANCE: LobbiesController;

    constructor() {
        this.lobbies = new Map<string,LobbyController>();
    }

    createLobby(): Result<LobbyID> {
        const id = createUniqueID(this.lobbies);
        this.lobbies.set(id, new LobbyController(id));
        return {
            data: { id }
        };
    }

    joinLobby(id: string): Result<LobbyID> {
        if (this.lobbies.has(id)) {
            const data = {id}
            return {data};
        }
        return {error: "No such ID"}
    }

    static getInstance(): LobbiesController {
        if(LobbiesController.INSTANCE === undefined) {
            LobbiesController.INSTANCE = new LobbiesController();
        }
        return LobbiesController.INSTANCE;
    }
}