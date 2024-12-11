<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="options" required="true" type="java.util.List" %>
<%@ attribute name="label" required="false" %>
<%@ attribute name="name" required="true" %>

<div class="select-wrapper">
    <label>${label}</label>
    <select name="${name}" required>
        <c:forEach items="${options}" var="option">
            <option value="${option}">${option}</option>
        </c:forEach>
    </select>
</div>
