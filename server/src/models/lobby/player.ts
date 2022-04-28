export class LobbyPlayer {
    private id: string;
    private ready: boolean;
    constructor(id: string) {
        this.id = id;
        this.ready = false;
    }

    getID(): string {
        return this.id;
    }

    getReady(): boolean {
        return this.ready;
    }

    toggleReady(): void {
        console.log("toggling ready from: " + this.ready);
        this.ready = !this.ready;
    }
}