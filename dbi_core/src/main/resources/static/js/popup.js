export function initPopup() {

    const wrapper = document.querySelectorAll(".popup-wrapper");
    const popup = document.querySelectorAll(".popup");
    const close = document.querySelectorAll(".popup-wrapper .close-popup");

    wrapper.forEach(wrap => {
        wrap.addEventListener("click", (event) => {
            wrapper.classList.toggle("hidden");
        })

        const popup = wrap.querySelector(".popup");
        const close = wrap.querySelector(".popup-wrapper .close-popup");

        close.addEventListener("click", (event) => {
            wrap.classList.toggle("hidden");
        })

        popup.addEventListener("click", (event) => {
            event.stopPropagation();
        })
    })

}