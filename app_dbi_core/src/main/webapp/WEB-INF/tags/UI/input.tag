<%@ attribute name="type" required="true" %>
<%@ attribute name="name" required="true" %>
<%@ attribute name="label" required="false" %>
<%@ attribute name="required" required="false" %>
<%@ attribute name="classList" required="false" %>
<%@ attribute name="placeholder" required="false" %>
<%@ attribute name="maxlength" required="false" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="input-wrapper ${classList}">
    <c:choose>
        <c:when test="${label != null}">
            <label><spring:message code="${label}"/></label>
        </c:when>
    </c:choose>
    <input required="${required}" name="${name}" type="${type}"
           placeholder="${placeholder}" ${maxlength ? 'maxlength="'+maxlength+'"' : ""}>
</div>