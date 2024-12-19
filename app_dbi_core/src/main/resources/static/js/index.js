import "./form.js"
import "./auth.js"

if (localStorage.getItem("clientId")) {
    if (page === "index") {
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

    if (page === "login"){
       import("./yandexAuth.js").then((module) => {
           module.initYandexAuth();
       })

    }
}


