<%@ taglib prefix="ui" tagdir="/WEB-INF/tags/UI" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<section class="client">
    <div class="client-wrapper">
        <div class="client-content">
            <h2><spring:message code="message.client.title" /></h2>

            <div class="client-info-wrapper">
                <p><spring:message code="message.client.info" /></p>
                <div class="client-info">
                    <div class="loader"></div>
                    <div class="info none">
                        <p id="phone"><spring:message code="message.form.phone" />: <span></span></p>
                        <p id="first-name"><spring:message code="message.form.first_name" />: <span></span></p>
                        <p id="last-name"><spring:message code="message.form.last_name" />: <span></span></p>
                    </div>
                </div>
            </div>
        </div>

        <div class="client-control">
            <ui:button classList="update-client" title="message.button.update" type="button"/>
            <ui:button classList="delete-client important" title="message.button.delete" type="button" />
        </div>

    </div>

    <ui:popup id="update">
        <form id="update-client" method="PATCH" action="/clients">

            <h2><spring:message code="message.client.update.title" /></h2>

            <ui:input name="id" type="hidden"/>
            <ui:input label="message.form.phone" name="phone" type="tel" placeholder="+7 (___) ___-__-__" maxlength="18"/>
            <ui:input label="message.form.first_name" name="firstName" type="text"/>
            <ui:input label="message.form.last_name" name="lastName" type="text"/>
            <ui:error/>
            <ui:button title="message.form.submit" type="submit"/>
        </form>
    </ui:popup>
    <ui:popup id="delete">
        <form id="delete-client" method="DELETE" action="/clients/{id}">

            <h2><spring:message code="message.client.delete.title" /></h2>
            <ui:error/>
            <ui:input name="id" type="hidden"/>
            <ui:button title="message.delete.submit" type="submit"/>
        </form>
    </ui:popup>
</section>
