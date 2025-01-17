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

export function infoAccount() {

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

            if (infoBlock) {
                if(transactions.length > 0) {

                    let element = infoBlock.querySelector(".ref div");
                    let wrapper = infoBlock.querySelector(".info")

                    wrapper.innerHTML = ''
                    infoBlock.querySelector('.loader').classList.add('none')

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

                        let cloneNode = element.cloneNode(true);

                        cloneNode.querySelector('#date').innerHTML = date
                        cloneNode.querySelector('#time').innerHTML = time
                        cloneNode.querySelector('#type').innerHTML = transaction.transactionType
                        cloneNode.querySelector('#amount').innerHTML = transaction.amount

                        let accountWrapper = cloneNode.querySelector('#transaction-account-element')
                        accountWrapper.querySelector('#name span').innerHTML = account.name
                        accountWrapper.querySelector('#currency span').innerHTML = account.currency

                        if(recipient){
                            let cloneAccountNode = accountWrapper.cloneNode(true);

                            cloneAccountNode.querySelector('#name span').innerHTML = account.name
                            cloneAccountNode.querySelector('#currency span').innerHTML = account.currency

                            cloneNode.querySelector('.transaction-account-wrapper').appendChild(cloneAccountNode);
                        }

                        wrapper.appendChild(cloneNode);
                    })

                } else {
                    // infoBlock.innerHTML = ''
                    // infoBlock.innerHTML += `<p>null transactions</p>`
                }

            }
        })
        .catch(e => {
            console.error("Request failed:", e.message);
        });
}