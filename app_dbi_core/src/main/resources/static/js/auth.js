let auth = true

let clientId = localStorage.getItem("clientId")

document.addEventListener("DOMContentLoaded", () => {
    const logout = document.querySelector("#sign-out");

    if (logout) {

        logout.addEventListener("click", () => {
            document.cookie = "";

            window.location.reload();
        })
    }
})