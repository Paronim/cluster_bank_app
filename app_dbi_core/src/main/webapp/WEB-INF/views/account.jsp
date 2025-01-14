<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<%@ taglib prefix="ui" tagdir="/WEB-INF/tags/UI" %>
<%@ taglib prefix="component" tagdir="/WEB-INF/tags/components"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html>
<tag:header title="DBI" page="${page}"/>
<body>
<section class="account">

    <div class="account-wrapper">
        <div class="account-content">

            <div class="account-info-wrapper">
                <h2><spring:message code="message.account.info"/></h2>

                <div
                        class="account-info"
                        data-id="${account.getId()}"
                        data-name="${account.getName()}"
                        data-type="${account.getType()}"
                        data-currency="${account.getCurrency()}"
                >
                    <p id="name"><spring:message code="message.form.name"/>: <span>${account.getName()}</span></p>
                    <p id="type"><spring:message code="message.form.type"/>: <span>${account.getType()}</span></p>
                    <p id="balance"><spring:message code="message.form.balance"/>: <span>${account.getBalance()} ${account.getCurrency()}</span></p>
                </div>


            </div>
            <div class="account-control">
                <ui:button classList="update-account " title="message.button.update" type="button"/>
                <ui:button classList="delete-account important" title="message.button.delete" type="button"/>
            </div>
        </div>

        <div class="transfer-control">
            <ui:button classList="withdraw-account " title="message.button.withdraw" type="button"/>
            <ui:button classList="deposit-account " title="message.button.deposit" type="button"/>
            <ui:button classList="transfer-account " title="message.button.transfer" type="button"/>
        </div>

    </div>

    <ui:popup id="update">
        <form id="update-account" method="PUT" action="/accounts">

            <h2><spring:message code="message.account.update.title"/></h2>

            <ui:input name="id" type="hidden"/>
            <ui:input label="message.form.name" name="name" type="text"/>
            <ui:select options="${currencies}" label="message.form.currency" name="currency"/>
            <ui:select options="${types}" label="message.form.type" name="type"/>
            <ui:error/>
            <ui:button title="message.form.submit" type="submit"/>
        </form>
    </ui:popup>

    <ui:popup id="delete">
        <form id="delete-account" method="DELETE" action="/accounts/{id}">
            <h2><spring:message code="message.account.delete.title"/></h2>
            <ui:error/>
            <ui:input name="id" type="hidden"/>
            <ui:button title="message.delete.submit" type="submit"/>
        </form>
    </ui:popup>

    <ui:popup id="transfer">
        <form id="transfer-account" method="POST" action="/transactions/transfer">
            <h2><spring:message code="message.account.transfer.title"/></h2>
            <ui:input name="accountId" type="hidden"/>
            <ui:input label="message.form.amount" name="amount" type="number"/>
            <ui:input label="message.form.recipient" name="recipientId" type="number"/>
            <ui:error/>
            <ui:button title="message.form.submit" type="submit"/>
        </form>
    </ui:popup>

    <ui:popup id="withdraw">
        <form id="withdraw-account" method="POST" action="/accounts/{id}/withdraw">
            <h2><spring:message code="message.account.withdraw.title"/></h2>
            <ui:input name="id" type="hidden"/>
            <ui:input label="message.form.amount" name="amount" type="number"/>
            <ui:error/>
            <ui:button title="message.form.submit" type="submit"/>
        </form>
    </ui:popup>

    <ui:popup id="deposit">
        <form id="deposit-account" method="POST" action="/accounts/{id}/deposit">
            <h2><spring:message code="message.account.deposit.title"/></h2>
            <ui:input name="id" type="hidden"/>
            <ui:input label="message.form.amount" name="amount" type="number"/>
            <ui:error/>
            <ui:button title="message.form.submit" type="submit"/>
        </form>
    </ui:popup>
</section>
<component:transaction />
</body>
</html>