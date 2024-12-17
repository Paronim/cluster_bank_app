<%@ attribute name="title" required="true" %>
<%@ attribute name="page" required="false" %>
<%@ taglib prefix="ui" tagdir="/WEB-INF/tags/UI" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<head>
    <title>DBI app</title>

    <link rel="stylesheet" href="/css/UI/button.css">
    <link rel="stylesheet" href="/css/UI/form.css">
    <link rel="stylesheet" href="/css/UI/input.css">
    <link rel="stylesheet" href="/css/UI/error.css">
    <link rel="stylesheet" href="/css/UI/loader.css">
    <link rel="stylesheet" href="/css/UI/popup.css">
    <link rel="stylesheet" href="/css/UI/select.css">
    <link rel="stylesheet" href="/css/components/account.css">
    <link rel="stylesheet" href="/css/components/accounts.css">
    <link rel="stylesheet" href="/css/components/client.css">
    <link rel="stylesheet" href="/css/components/transaction.css">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@24,400,0,0&icon_names=close" />
    <link rel="stylesheet" href="/css/style.css">
    <script>
        var page = "${page}"
    </script>
    <script type="module" src="/js/index.js"></script>
</head>
<header>
    <div class="header-wrapper">
        <p class="logo"><a href="/">${title}</a></p>
        <c:choose>
            <c:when test="${page == 'login'}">
                <a href="/register" id="Register" class="control-link">Register</a>
            </c:when>
            <c:when test="${page == 'register'}">
                <a href="/login" id="Sign up" class="control-link">Sign up</a>
            </c:when>
            <c:otherwise>
                <a href="/auth/logout" id="Logout" class="control-link">Logout</a>
            </c:otherwise>
        </c:choose>
    </div>
</header>
