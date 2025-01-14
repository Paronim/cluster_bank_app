<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html>
<tag:header title="DBI" page="${page}"/>
<body>
<section class="error">
    <h1><spring:message code="message.error.title" /></h1>
    <p>${error}</p>
</section>
</body>
</html>