<%@ attribute name="type" required="true" %>
<%@ attribute name="name" required="true" %>
<%@ attribute name="label" required="false" %>
<%@ attribute name="required" required="false" %>
<%@ attribute name="classList" required="false" %>
<%@ attribute name="placeholder" required="false" %>
<%@ attribute name="maxlength" required="false" %>

<div class="input-wrapper ${classList}">
    <label>${label}</label>
    <input required="${required}" name="${name}" type="${type}" placeholder="${placeholder}" ${maxlength ? 'maxlength="'+maxlength+'"' : ""}>
</div>