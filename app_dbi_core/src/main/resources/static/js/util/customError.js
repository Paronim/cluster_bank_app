export default class CustomError extends Error {
    constructor(message, details) {
        super(message);
        this.details = details;
        this.name = "Error";
    }
}
