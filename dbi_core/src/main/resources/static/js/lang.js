import {changeLang} from "./fetch/lang.js";

export function initLang(){

    const langWrapper = document.getElementById("lang");
    let lang = langWrapper.getAttribute("data-lang");
    let langSelect = langWrapper.querySelector('select');

    langSelect.value = lang;
    langSelect.addEventListener('change', async event => {
        const selectedValue = event.target.value;
        let res = await changeLang(selectedValue);

        if(res) window.location.reload();
    })
}