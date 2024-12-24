let url = "http://" + window.location.host + "/clients";

export async function getClient(clientId) {

    try {
        let response = await fetch(url + "/" + clientId, {
            method: "GET"
        })

        if (response.ok) {
            return response.json();
        } else {
            throw new Error("Could not find client info");
        }
    } catch (e) {
        console.error(e)
    }
}

export async function getClientId() {

    try {
        let response = await fetch(url + "/id", {
            method: "GET"
        })

        if (response.ok) {
            return response.json();
        } else {
            throw new Error("Could not find client info");
        }
    } catch (e) {
        console.error(e)
    }
}