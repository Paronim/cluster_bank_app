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


function callbackCreate(){
    const button =  document.querySelector(".create-account");
    info()
    showNotification("Create account successfully")
    button.click();
}

function info() {
    const infoBlock = document.querySelector(".accounts-info");

    getAccounts(clientId)
        .then(accounts => {
            if (infoBlock && accounts) {
                infoBlock.innerHTML = ''
                accounts.forEach(account => {
                    infoBlock.innerHTML += `<a href="/account/${account.id}" class="accounts-element">
                        <div>
                            <p>Name: ${account.name}</p>
                            <p>Type: ${account.type}</p>
                            <p>Balance: ${account.balance} ${account.currency}</p>
                        </div>
                        
                      </a>
                    `;
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
