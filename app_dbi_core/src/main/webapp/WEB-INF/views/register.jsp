<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<%@ taglib prefix="ui" tagdir="/WEB-INF/tags/UI" %>

<!DOCTYPE html>
<html>
<tag:header title="DBI" page="${page}"/>
<body>
<section class="reg">
    <form id="reg" method="POST" action="/auth/register">

        <h2>Registration</h2>

        <ui:input label="Phone" required="true" name="phone" type="tel" placeholder="+7 (___) ___-__-__" maxlength="18"/>
        <ui:input label="First name" required="true" name="firstName" type="text"/>
        <ui:input label="Last name" required="true" name="lastName" type="text"/>
        <ui:input label="Password" required="true" name="password" type="text"/>
        <ui:error/>
        <ui:button title="Submit" type="submit"/>
    </form>
</section>
</body>
</html>