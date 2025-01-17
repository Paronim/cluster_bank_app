<%@ attribute name="title" required="true" %>
<%@ attribute name="type" required="true" %>
<%@ attribute name="classList" required="false" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<button class="button ${classList}" type="${type}"><spring:message code="${title}" /></button>