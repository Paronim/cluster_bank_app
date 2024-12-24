import "./form.js"
import "./auth.js"
import {getClientId} from "./fetch/clients.js";

initModules()

async function initModules() {
    if (page === "index") {
        await getId()

        import("./client.js").then((module) => {
            module.initClient()
        })
        import("./accounts.js").then((module) => {
            module.initAccounts()
        })
        import("./transaction.js").then((module) => {
            module.initAllTransactions()
        })
    }

    if (page === "account") {
        await getId()

        import("./account.js").then((module) => {
            module.initAccount()
        })
        import("./transaction.js").then((module) => {
            module.initTransactions()
        })
    }

    if (page !== "error") {
        import("./popup.js").then((module) => {
            module.initPopup()
        })
    }
}

function getId() {
    if(!localStorage.getItem("clientId"))
        getClientId().then(clientId => {
            console.log(clientId)
            localStorage.setItem("clientId", clientId.id)
        })
}




