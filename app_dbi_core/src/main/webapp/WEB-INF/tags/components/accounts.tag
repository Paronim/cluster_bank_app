<%@ taglib prefix="ui" tagdir="/WEB-INF/tags/UI" %>
<%@ attribute name="currencies" required="true" type="java.util.List" %>

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

    <ui:popup id="create">
        <form id="create-account" method="POST" action="/accounts">
            <h2>Create account</h2>
            <ui:input name="clientId" type="hidden"/>
            <ui:input label="Name" name="name" type="text"/>
            <ui:select options="${currencies}" label="Currens" name="currency"/>
            <ui:error/>
            <ui:button title="Submit" type="submit"/>
        </form>
    </ui:popup>

</section>
