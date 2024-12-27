import {subscribe} from "./form.js";
import {getClient} from "./fetch/clients.js";
import {showNotification} from "./form.js"

const clientId = localStorage.getItem("clientId");

export function initClient() {
    info()
    updateClient()
    deleteClient()
    subscribe("update-client", callbackUpdate)
    subscribe("delete-client", callbackDelete)
}

function callbackUpdate(){
    const button =  document.querySelector(".update-client");
    info()
    showNotification("Update client successfully")
    button.click();
}

function callbackDelete(){
    localStorage.removeItem("clientId");
    window.location.reload()
}

function info(){
    const infoBlock = document.querySelector(".client-info");

    getClient(clientId)
        .then(client => {

            if (infoBlock && client) {
                infoBlock.setAttribute("data-firstName", client.firstName)
                infoBlock.setAttribute("data-lastName", client.lastName)
                infoBlock.setAttribute("data-phone", client.phone)
                infoBlock.innerHTML = `
                    <p>Phone: ${formatPhoneNumber(String(client.phone))}</p>
                    <p>First Name: ${client.firstName}</p>
                    <p>Last Name: ${client.lastName}</p>
                `;
            }
        })
        .catch(e => {
            console.error("Request failed:", e.message);
        });
}

function formatPhoneNumber(phoneNumber) {
    return `+7 (${phoneNumber.substring(1, 4)}) ${phoneNumber.substring(4, 7)} ${phoneNumber.substring(7, 9)}-${phoneNumber.substring(9)}`;
}

function updateClient() {
    const button =  document.querySelector(".update-client");
    const popup = document.querySelector(".client #update");
    const form = popup.querySelector('#update-client');
    button.addEventListener("click", event => {
        const data = document.querySelector(".client-info");
        popup.classList.toggle("hidden");
        form.querySelector('[name="id"]').value = localStorage.getItem("clientId");
        form.querySelector('[name="phone"]').value = formatPhoneNumber(data.getAttribute("data-phone"));
        form.querySelector('[name="firstName"]').value = data.getAttribute("data-firstName");
        form.querySelector('[name="lastName"]').value = data.getAttribute("data-lastName");
    })
}

function deleteClient(){
    const button =  document.querySelector(".delete-client");
    const popup = document.querySelector(".client #delete");
    const form = popup.querySelector('#delete-client');
    button.addEventListener("click", event => {
        const data = document.querySelector(".client-info");
        popup.classList.toggle("hidden");
        form.querySelector('[name="id"]').value = localStorage.getItem("clientId");
    })
}

