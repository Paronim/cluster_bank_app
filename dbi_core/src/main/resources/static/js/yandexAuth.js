
export function initYandexAuth() {
    window.YaAuthSuggest.init(
        {
            client_id: "01cff104a423420ba6c8637bc7b16166",
            response_type: "code",
            redirect_uri: window.location.origin + "/login/yandex"
        },
        window.location.origin,
        {
            view: "button",
            parentId: "yandex-auth",
            buttonSize: 'm',
            buttonView: 'main',
            buttonTheme: 'light',
            buttonBorderRadius: "10",
            buttonIcon: 'ya',
        }
    )
        .then(({handler}) => handler())
        .then(data => console.log('token', data))
        .catch(error => console.log('error', error))
}