export function initAuth() {

    const logoutElement = document.querySelector("#Logout")

    if(logoutElement){
        logoutElement.addEventListener("click", event => {
            localStorage.removeItem("clientId");
        })
    }
}