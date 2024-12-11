import {subscribe} from "./form.js";
import {getClient} from "./fetch/clients.js";

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
    button.click();
}

function callbackDelete(){
    localStorage.removeItem("clientId");
    window.location.href = "/auth";
}

function info(){
    const infoBlock = document.querySelector(".client-info");

    getClient(clientId)
        .then(client => {

            if (infoBlock && client) {
                infoBlock.setAttribute("data-firstName", client.firstName)
                infoBlock.setAttribute("data-lastName", client.lastName)
                infoBlock.innerHTML = `
                    <p>First Name: ${client.firstName}</p>
                    <p>Last Name: ${client.lastName}</p>
                `;
            }
        })
        .catch(e => {
            console.error("Request failed:", e.message);
        });
}

function updateClient() {
    const button =  document.querySelector(".update-client");
    const popup = document.querySelector(".client #update");
    const form = popup.querySelector('#update-client');
    button.addEventListener("click", event => {
        const data = document.querySelector(".client-info");
        popup.classList.toggle("hidden");
        form.querySelector('[name="id"]').value = localStorage.getItem("clientId");
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

