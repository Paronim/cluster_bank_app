<%@ taglib prefix="ui" tagdir="/WEB-INF/tags/UI" %>

<section class="client">
    <div class="client-wrapper">
        <div class="client-content">
            <h2>Client</h2>

            <div class="client-info-wrapper">
                <p>Client information</p>
                <div class="client-info">
                    <div class="loader"></div>
                </div>
            </div>
        </div>

        <div class="client-control">
            <ui:button classList="update-client" title="Update" type="button"/>
            <ui:button classList="delete-client important" title="Delete" type="button" />
        </div>

    </div>

    <ui:popup id="update">
        <form id="update-client" method="PUT" action="/clients">

            <h2>Update client information</h2>

            <ui:input name="id" type="hidden"/>
            <ui:input label="First name" name="firstName" type="text"/>
            <ui:input label="Last name" name="lastName" type="text"/>
            <ui:error/>
            <ui:button title="Submit" type="submit"/>
        </form>
    </ui:popup>
    <ui:popup id="delete">
        <form id="delete-client" method="DELETE" action="/clients/{id}">

            <h2>Are you sure you want to delete the client?</h2>
            <ui:error/>
            <ui:input name="id" type="hidden"/>
            <ui:button title="Submit" type="submit"/>
        </form>
    </ui:popup>
</section>
