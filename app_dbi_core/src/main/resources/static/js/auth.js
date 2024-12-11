let auth = true

let clientId = localStorage.getItem("clientId")

if (!clientId && window.location.pathname !== "/auth") {
    window.location.href = "/auth";
}

if (clientId && window.location.pathname === "/auth") {
    window.location.href = "/";
}

document.addEventListener("DOMContentLoaded", () => {
    const authForm = document.querySelector(".auth");
    const regForm = document.querySelector(".reg");
    let button = document.querySelector(".auth-button");

    if (button) {
        button.addEventListener("click", event => {
            auth = !auth;

            if (auth) {
                regForm.style.display = "none";
                authForm.style.display = "block";
                button.innerHTML = "Register";
            } else {
                regForm.style.display = "block";
                authForm.style.display = "none";
                button.innerHTML = "Sign up";
            }
        })
    } else {
        button =  document.querySelector(".main-button");
        button.addEventListener("click", event => {
            localStorage.removeItem("clientId")
            window.location.href = "/auth";
        })
    }
})