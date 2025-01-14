import CustomError from "./util/customError.js"
import Observable from "./util/observable.js"

const observable = new Observable();

document.addEventListener("DOMContentLoaded", () => {

    phoneMask()

    let forms = document.querySelectorAll("form");

    forms.forEach(form => {
        form.addEventListener("submit", event => {
            event.preventDefault();

            let formData = new FormData(form);
            let phone = formData.get("phone");
            if(phone){
                formData.set('phone', phone.replace(/\D/g, ""))
            }

            const submitButton = form.querySelector("button[type='submit']");
            const error = form.querySelector(".error-wrapper");

            submitButton.disabled = true;

            fetchData(form.getAttribute("method"), form.getAttribute("action"), formData).then(
                response => {
                    console.log(response.message);
                    observable.notify(form.getAttribute("id"), response.message)
                    if (form.getAttribute("id") === "auth") {
                        saveInfo(response.id)
                    }
                    error.setAttribute("style", "display: none")
                    error.innerHTML = ""

                    if(response.redirect){
                        window.location.href = response.redirect
                    }

                    submitButton.disabled = false;
                }
            ).catch(e => {
                submitButton.disabled = false;

                error.setAttribute("style", "display: block")
                error.innerHTML = `<p class='error'>${e.message}</p>`

                if (e.details) {
                    e.details.forEach(detail => {
                        error.insertAdjacentHTML("beforeend", `<p class='error'>${detail}</p>`);
                    });
                }
            })

        })
    })
})

function saveInfo(clientId) {
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
                method: method,
                redirect: "manual"
            })
        } else {
            response = await fetch(url, {
                method: method,
                headers: {
                    'Content-Type': 'application/json',
                },
                redirect: "manual",
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

function phoneMask() {
    const phoneInputs = document.querySelectorAll('[name="phone"]');

    phoneInputs.forEach(phoneInput => {

        phoneInput.addEventListener("input", (e) => {
            let input = e.target.value.replace(/\D/g, "");
            let formattedInput = "+7";

            if (input.length > 1) {
                formattedInput += ` (${input.substring(1, 4)}`;
            }
            if (input.length >= 5) {
                formattedInput += `) ${input.substring(4, 7)}`;
            }
            if (input.length >= 8) {
                formattedInput += `-${input.substring(7, 9)}`;
            }
            if (input.length >= 10) {
                formattedInput += `-${input.substring(9, 11)}`;
            }

            e.target.value = formattedInput;
        });
    })
}

export function subscribe(key, callback) {
    observable.subscribe(key, callback)
}

export function showNotification(message) {
    const container = document.getElementById('notification-container');

    const notification = document.createElement('div');
    notification.className = 'notification';
    notification.innerText = message;

    container.appendChild(notification);

    setTimeout(() => {
        notification.remove();
    }, 5000);
}

