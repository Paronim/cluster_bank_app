<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<%@ taglib prefix="ui" tagdir="/WEB-INF/tags/UI" %>

<!DOCTYPE html>
<html>
<tag:header title="DBI" page="${page}"/>
<body>
    <section class="auth">
        <form id="auth" method="GET" action="/clients/{id}">

            <h2>Auth</h2>

            <ui:input label="Client id" required="true" name="id" type="number"/>
            <ui:error/>
            <ui:button title="Submit" type="submit"/>
        </form>
    </section>
    <section class="reg" style="display: none">
        <form id="reg" method="POST" action="/clients">

            <h2>Registration</h2>

            <ui:input label="First name" required="true" name="firstName" type="text"/>
            <ui:input label="Last name" required="true" name="lastName" type="text"/>
            <ui:error/>
            <ui:button title="Submit" type="submit"/>
        </form>
    </section>
</body>
</html>