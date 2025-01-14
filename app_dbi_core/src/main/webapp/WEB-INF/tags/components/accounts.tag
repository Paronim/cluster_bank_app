<%@ taglib prefix="ui" tagdir="/WEB-INF/tags/UI" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ attribute name="currencies" required="true" type="java.util.List" %>

<section class="accounts">
    <div class="accounts-wrapper">
        <div class="accounts-content">
            <h2><spring:message code="message.account.title" /></h2>

            <div class="accounts-info-wrapper">
                <p><spring:message code="message.accounts.title" /></p>
                <div class="accounts-info">
                    <div class="loader"></div>
                    <div class="ref none">
                        <a href="/" class="accounts-element">
                            <p id="name"><spring:message code="message.form.name" />: <span></span></p>
                            <p id="type"><spring:message code="message.form.type" />: <span></span></p>
                            <p id="balance"><spring:message code="message.form.balance" />: <span></span></p>
                        </a>
                    </div>
                    <div class="info"></div>
                </div>
            </div>
        </div>

        <div class="accounts-control">
            <ui:button classList="create-account transfer" title="message.button.create" type="button"/>
        </div>

    </div>

    <ui:popup id="create">
        <form id="create-account" method="POST" action="/accounts">
            <h2><spring:message code="message.account.create.title" /></h2>
            <ui:input name="clientId" type="hidden"/>
            <ui:input label="message.form.name" name="name" type="text"/>
            <ui:select options="${currencies}" label="message.form.currency" name="currency"/>
            <ui:error/>
            <ui:button title="message.form.submit" type="submit"/>
        </form>
    </ui:popup>

</section>
