import CustomError from "./util/customError.js"
import Observable from "./util/observable.js"

const observable = new Observable();

document.addEventListener("DOMContentLoaded", () => {

    let forms = document.querySelectorAll("form");

    forms.forEach(form => {
        form.addEventListener("submit", event => {
            event.preventDefault();

            let formData = new FormData(form);

            const error = form.querySelector(".error-wrapper");

            fetchData(form.getAttribute("method"), form.getAttribute("action"), formData).then(
                response => {
                    observable.notify(form.getAttribute("id"))
                    if (form.getAttribute("id") === "auth" || form.getAttribute("id") === "reg") {
                        saveClient(response.id)
                        window.location.href = "/";
                    }
                    error.setAttribute("style", "display: none")
                    error.innerHTML = ""

                }
            ).catch(e => {
                error.setAttribute("style", "display: block")
                error.innerHTML = `<p class='error'>${e.message}</p>`

                if(e.details){
                    e.details.forEach(detail => {
                        error.insertAdjacentHTML("beforeend", `<p class='error'>${detail}</p>`);
                    });
                }
            })

        })
    })
})

function saveClient(clientId) {
    localStorage.setItem("clientId", clientId);
}

function populateUrl(urlTemplate, formData) {
    const params = Object.fromEntries(formData.entries());
    return urlTemplate.replace(/\{(.*?)}/g, (match, key) => {
        if (params[key]) {
            return encodeURIComponent(params[key]);
        } else {
            console.error(`Missing parameter: ${key}`);
            return match;
        }
    });
}

async function fetchData(method, action, data) {

    let url = populateUrl("http://" + window.location.host + action, data)

    try {
        let response;
        if (method === "GET" || method === "DELETE") {
            response = await fetch(url, {
                method: method
            })
        } else {
            response = await fetch(url, {
                method: method,
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(Object.fromEntries(data))
            })
        }

        if (!response.ok) {
            console.log(`HTTP error! Status: ${response.status}`);
            let error = await response.json()
            throw new CustomError(error.message, error.details)
        } else {
            return await response.json()
        }

    } catch (e) {
        console.error('Request failed:', e.message);
        throw e;
    }

}

export function subscribe(key, callback){
    observable.subscribe(key, callback)
}