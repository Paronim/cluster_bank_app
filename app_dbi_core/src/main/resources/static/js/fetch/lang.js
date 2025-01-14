let url = "http://" + window.location.host + "/change/lang/";

export async function changeLang(lang) {

    try {
        let response = await fetch(url, {
            method: "POST",
            body: lang
        })

        console.log("Response:", response)

        if (response.ok) {
            return true;
        } else {
            return false;
        }
    } catch (e) {
        console.error(e)
    }
}