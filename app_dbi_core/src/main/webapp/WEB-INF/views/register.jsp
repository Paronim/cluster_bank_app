<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<%@ taglib prefix="ui" tagdir="/WEB-INF/tags/UI" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html>
<tag:header title="DBI" page="${page}"/>
<body>
<section class="reg">
    <form id="reg" method="POST" action="/auth/register">

        <h2><spring:message code="message.register.title" /></h2>

        <ui:input label="message.form.phone" required="true" name="phone" type="tel" placeholder="+7 (___) ___-__-__" maxlength="18"/>
        <ui:input label="message.form.first_name" required="true" name="firstName" type="text"/>
        <ui:input label="message.form.last_name" required="true" name="lastName" type="text"/>
        <ui:input label="message.form.password" required="true" name="password" type="text"/>
        <ui:error/>
        <ui:button title="message.form.submit" type="submit"/>
    </form>
</section>
</body>
</html>