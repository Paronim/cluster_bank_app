<%@ attribute name="id" required="true" %>

<div id="${id}" class="popup-wrapper hidden">

    <div class="popup">
        <span class="material-symbols-outlined close-popup">close</span>
        <jsp:doBody/>
    </div>

</div>