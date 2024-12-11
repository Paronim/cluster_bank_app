import {getAccounts, getAccount} from "./fetch/accounts.js";
import {getTransaction} from "./fetch/transactions.js";

const clientId = localStorage.getItem("clientId");

export function initAllTransactions() {
    infoAll()
}

export function initTransactions() {
    infoAccount()
}

function infoAll() {

    getAccounts(clientId).then(
        accounts => {
            accounts.forEach(account => {
                renderTransactions(account)
            })
        }
    ).catch(e => {
        console.error("Request failed:", e.message);
    });
}

function infoAccount() {

    const accountData = document.querySelector(".account-info");

    const account = {
        id: accountData.getAttribute("data-id"),
        name: accountData.getAttribute("data-name"),
        currency: accountData.getAttribute("data-currency")
    }

    renderTransactions(account);

}


function renderTransactions(account) {
    const infoBlock = document.querySelector(".transaction-info");

    getTransaction(account.id)
        .then(transactions => {

            if (infoBlock && transactions) {
                infoBlock.innerHTML = ''
                transactions.forEach(async transaction => {

                    let dateObj = new Date(transaction.createdAt);
                    const date = dateObj.toISOString().split("T")[0];
                    const time = dateObj.toTimeString().split(" ")[0];

                    let recipient;
                    if (transaction.recipientId) {
                        recipient = await getAccount(transaction.recipientId).then(recipient => {
                            return recipient
                        }).catch(e => {
                            console.error("Request failed:", e.message);
                        });
                    }

                    infoBlock.innerHTML += `<div class="transaction-element">

                            <div>
                                <p>${date}</p> 
                                <p>${time}</p>
                            </div>
                            <div>
                                <p>Type:</p> 
                                <p>${transaction.transactionType}</p>
                            </div>
                            <div>
                                <p>Amount:</p> 
                                <p>${transaction.amount}</p>
                            </div>
                            <div class="transaction-account-wrapper">
                                <p>Account:</p>
                                <p>Name: ${account.name} Currency: ${account.currency}</p>
                                ${recipient ? "<p>Name: " + recipient.name + " Currency: " + recipient.currency + "</p>" : ""}
                            </div>
                      </div>
                    `;
                })


            }
        })
        .catch(e => {
            console.error("Request failed:", e.message);
        });
}