let url = "http://" + window.location.host + "/transactions";

export async function getTransaction(accountId) {

    try {
        let response = await fetch(url + "?sid=" + accountId, {
            method: "GET"
        })

        if (response.ok) {
            return response.json();
        } else {
            throw new Error("Could not find transaction info");
        }
    } catch (e) {
        console.error(e)
    }
}