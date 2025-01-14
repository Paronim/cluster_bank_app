<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<%@ taglib prefix="ui" tagdir="/WEB-INF/tags/UI" %>

<!DOCTYPE html>
<html>
<tag:header title="DBI" page="${page}"/>
<body>
<section class="auth">
    <form id="auth" method="POST" action="/auth/login">

        <h2><spring:message code="message.login.title" /></h2>

        <ui:input label="message.form.phone" required="true" name="phone" type="tel" placeholder="+7 (___) ___-__-__" maxlength="18"/>
        <ui:input label="message.form.password" required="true" name="password" type="text"/>

        <ui:error/>
        <a id="yandex-auth" href="/oauth2/authorization/yandex">Yandex</a>
        <ui:button title="message.form.submit" type="submit"/>
    </form>
</section>
</body>
</html>