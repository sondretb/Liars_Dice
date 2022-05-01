export function createID(): string {
    let id = "";
    const characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    while (id.length < 5) {
        const charIndex = Math.round(Math.random() * (characters.length - 1))
        id += characters[charIndex];
    }
    return id;
}

export function createUniqueID(currentIDs: Map<string, unknown>) {
    let id = createID();
    while (currentIDs.has(id)){
        id = createID();
    }
    return id;
}

export interface Result<Data = undefined> {
    data?: Data;
    error?: string;
}