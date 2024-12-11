<%@ taglib prefix="ui" tagdir="/WEB-INF/tags/UI" %>
<%@ attribute name="currensies" required="true" type="java.util.List" %>
<%@ attribute name="types" required="true" type="java.util.List" %>

<section class="accounts">
    <div class="accounts-wrapper">
        <div class="accounts-content">
            <h2>Account</h2>

            <div class="accounts-info-wrapper">
                <p>All accounts:</p>
                <div class="accounts-info">
                    <div class="loader"></div>
                </div>
            </div>
        </div>

        <div class="accounts-control">
            <ui:button classList="create-account transfer" title="Create" type="button"/>
        </div>

    </div>

</section>
