<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
    <meta charSet="utf-8" />
    <meta name='viewport' content='width=device-width, initial-scale=1, maximum-scale=1, minimum-scale=1, shrink-to-fit=no, viewport-fit=cover'>
    <meta http-equiv='X-UA-Compatible' content='ie=edge'>
    <style>
        html,
        body {
            background: #eee;
        }
    </style>
</head>
<body>
    <script>
        window.onload = function() {
            const queryString = window.location.search;
            console.log(${state})
            fetch(window.location.origin + "/login/oauth2/code/yandex" + queryString + "&state=${state}", {
                method: "GET"
            }).then(() => {
                window.close()
            })
        };
    </script>
</body>
</html>