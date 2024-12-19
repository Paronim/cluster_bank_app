<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<%@ taglib prefix="ui" tagdir="/WEB-INF/tags/UI" %>

<!DOCTYPE html>
<html>
<tag:header title="DBI" page="${page}"/>
<body>
<section class="auth">
    <form id="auth" method="POST" action="/auth/login">

        <h2>Auth</h2>

        <ui:input label="Phone" required="true" name="phone" type="tel" placeholder="+7 (___) ___-__-__" maxlength="18"/>
        <ui:input label="Password" required="true" name="password" type="text"/>

        <ui:error/>
        <div id="yandex-auth"></div>
        <ui:button title="Submit" type="submit"/>
    </form>
</section>
</body>
</html>