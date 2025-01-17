<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ attribute name="options" required="true" type="java.util.List" %>
<%@ attribute name="label" required="false" %>
<%@ attribute name="name" required="true" %>

<div class="select-wrapper">
    <c:choose>
        <c:when test="${label != null}">
            <label><spring:message code="${label}"/></label>
        </c:when>
    </c:choose>
    <select name="${name}" required>
        <c:forEach items="${options}" var="option">
            <option value="${option}">${option}</option>
        </c:forEach>
    </select>
</div>
