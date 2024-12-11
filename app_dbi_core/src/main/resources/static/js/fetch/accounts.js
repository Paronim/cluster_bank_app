const url = "http://" + window.location.host + "/accounts";

export async function getAccounts(clientId) {

    try {
        let response = await fetch(url + "?cid=" + clientId, {
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

export async function getAccount(accountId) {

    try {
        let response = await fetch(url + "/" + accountId, {
            method: "GET"
        })

        if (response.ok) {
            return response.json();
        } else {
            throw new Error("Could not find account info");
        }
    } catch (e) {
        console.error(e)
    }
}