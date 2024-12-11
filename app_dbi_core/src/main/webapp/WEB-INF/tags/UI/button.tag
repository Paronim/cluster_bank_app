<%@ attribute name="title" required="true" %>
<%@ attribute name="type" required="true" %>
<%@ attribute name="classList" required="false" %>

<button class="button ${classList}" type="${type}">${title}</button>