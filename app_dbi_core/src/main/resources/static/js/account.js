import {subscribe} from "./form.js";
import {getAccount} from "./fetch/accounts.js";

export function initAccount() {
    info()
    subscribe("update-account", callbackUpdate)
    subscribe("delete-account", callbackDelete)
    subscribe("transfer-account", callbackTransfer)
    subscribe("withdraw-account", callbackWithdraw)
    subscribe("deposit-account", callbackDeposit)
}

function callbackUpdate() {
    const button = document.querySelector(".update-account");
    updateInfo()
    button.click();
}

function callbackDelete() {
    window.location.href = "/";
}

function callbackTransfer() {
    const button = document.querySelector(".transfer-account");
    updateInfo()
    button.click();
}

function callbackWithdraw() {
    const button = document.querySelector(".withdraw-account");
    updateInfo()
    button.click();
}

function callbackDeposit() {
    const button = document.querySelector(".deposit-account");
    updateInfo()
    button.click();
}

function info() {
    updateAccount()
    deleteAccount()
    transferAccount()
    withdrawAccount()
    depositAccount()
}

function updateInfo() {

    const infoBlock = document.querySelector(".account-info");

    getAccount(infoBlock.getAttribute("data-id"))
        .then(account => {
            if (infoBlock && account) {
                infoBlock.innerHTML = `
                            <p>Name: ${account.name}</p>
                            <p>Type: ${account.type}</p>
                            <p>Balance: ${account.balance} ${account.currency}</p>
                    `;
            }
        })
        .catch(e => {
            console.error("Request failed:", e.message);
        });
}

function updateAccount() {
    const button = document.querySelector(".update-account");
    const popup = document.querySelector(".account #update");
    const form = document.querySelector('#update-account');
    button.addEventListener("click", event => {
        popup.classList.toggle("hidden");
        const data = document.querySelector(".account-info")
        form.querySelector('[name="id"]').value = data.getAttribute("data-id");
        form.querySelector('[name="name"]').value = data.getAttribute("data-name");
        form.querySelector('[name="currency"]').value = data.getAttribute("data-currency");
        form.querySelector('[name="type"]').value = data.getAttribute("data-type");
    })
}

function deleteAccount() {
    const button = document.querySelector(".delete-account");
    const popup = document.querySelector(".account #delete");
    const form = document.querySelector('#delete-account');
    button.addEventListener("click", event => {
        popup.classList.toggle("hidden");
        const data = document.querySelector(".account-info")
        form.querySelector('[name="id"]').value = data.getAttribute("data-id");
    })
}

function transferAccount() {
    const button = document.querySelector(".transfer-account");
    const popup = document.querySelector(".account #transfer");
    const form = document.querySelector('#transfer-account');
    button.addEventListener("click", event => {
        popup.classList.toggle("hidden");
        const data = document.querySelector(".account-info")
        form.querySelector('[name="accountId"]').value = data.getAttribute("data-id");
        form.querySelector('[name="amount"]').value = 0;
    })
}

function withdrawAccount() {
    const button = document.querySelector(".withdraw-account");
    const popup = document.querySelector(".account #withdraw");
    const form = document.querySelector('#withdraw-account');
    button.addEventListener("click", event => {
        popup.classList.toggle("hidden");
        const data = document.querySelector(".account-info")
        form.querySelector('[name="id"]').value = data.getAttribute("data-id");
        form.querySelector('[name="amount"]').value = 0;
    })
}

function depositAccount() {
    const button = document.querySelector(".deposit-account");
    const popup = document.querySelector(".account #deposit");
    const form = document.querySelector('#deposit-account');
    button.addEventListener("click", event => {
        popup.classList.toggle("hidden");
        const data = document.querySelector(".account-info")
        form.querySelector('[name="id"]').value = data.getAttribute("data-id");
        form.querySelector('[name="amount"]').value = 0;
    })
}