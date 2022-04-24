
export function createID(): string {
    let id = "";
    const characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    while (id.length < 5) {
        const charIndex = Math.round(Math.random() * (characters.length - 1))
        id += characters[charIndex];
    }
    return id;
}