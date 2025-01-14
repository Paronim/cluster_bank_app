import {subscribe} from "./form.js";
import {getAccounts} from "./fetch/accounts.js";
import {showNotification} from "./form.js"

let url = "http://" + window.location.host + "/accounts";

const clientId = localStorage.getItem("clientId");

export function initAccounts() {
    info()
    createAccount()
    subscribe("create-account", callbackCreate)
}


function callbackCreate(message){
    const button =  document.querySelector(".create-account");
    info()
    showNotification(message)
    button.click();
}

function info() {
    const infoBlock = document.querySelector(".accounts-info");

    getAccounts(clientId)
        .then(accounts => {
            if (infoBlock && accounts) {

                let element = infoBlock.querySelector(".ref a");
                let wrapper = infoBlock.querySelector(".info")

                wrapper.innerHTML = ''
                infoBlock.querySelector('.loader').classList.add('none')

                accounts.forEach(account => {
                    let cloneNode = element.cloneNode(true);

                    cloneNode.setAttribute("href", `/account/${account.id}`);
                    cloneNode.querySelector("#name span").innerHTML = account.name;
                    cloneNode.querySelector("#type span").innerHTML = account.type;
                    cloneNode.querySelector("#balance span").innerHTML = `${account.balance} ${account.currency}`

                    wrapper.appendChild(cloneNode);
                })

            }
        })
        .catch(e => {
            console.error("Request failed:", e.message);
        });
}

function createAccount() {
    const button =  document.querySelector(".create-account");
    const popup = document.querySelector(".accounts #create");
    const form = document.querySelector('#create-account');
    button.addEventListener("click", event => {
        popup.classList.toggle("hidden");
        const data = button.parentNode.parentNode;
        form.querySelector('[name="clientId"]').value = localStorage.getItem("clientId");
    })

}
