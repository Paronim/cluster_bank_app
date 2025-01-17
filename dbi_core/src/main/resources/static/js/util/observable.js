export default class Observable {
    constructor() {
        this.subscriptions = new Map();
    }

    subscribe(key, callback) {
        if (typeof callback === 'function') {
            this.subscriptions.set(key, callback);
        }
    }

    notify(key, message) {
        const callback = this.subscriptions.get(key)

        if (typeof callback === 'function') {
            callback(message)
        }
    }
}