<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<%@ taglib prefix="ui" tagdir="/WEB-INF/tags/UI" %>
<%@ taglib prefix="component" tagdir="/WEB-INF/tags/components"%>

<!DOCTYPE html>
<html>
<tag:header title="DBI" page="${page}"/>
<body>
<section class="account">

    <div class="account-wrapper">
        <div class="account-content">

            <div class="account-info-wrapper">
                <h2>Account</h2>

                <div
                        class="account-info"
                        data-id="${account.getId()}"
                        data-name="${account.getName()}"
                        data-type="${account.getType()}"
                        data-currency="${account.getCurrency()}"
                >
                    <p>Name: ${account.getName()}</p>
                    <p>Type: ${account.getType()}</p>
                    <p>Balance: ${account.getBalance()} ${account.getCurrency()}</p>
                </div>


            </div>
            <div class="account-control">
                <ui:button classList="update-account " title="Update" type="button"/>
                <ui:button classList="delete-account important" title="Delete" type="button"/>
            </div>
        </div>

        <div class="transfer-control">
            <ui:button classList="withdraw-account " title="Withdraw" type="button"/>
            <ui:button classList="deposit-account " title="Deposit" type="button"/>
            <ui:button classList="transfer-account " title="Transfer" type="button"/>
        </div>

    </div>

    <ui:popup id="update">
        <form id="update-account" method="PUT" action="/accounts">

            <h2>Update account information</h2>

            <ui:input name="id" type="hidden"/>
            <ui:input label="Name" name="name" type="text"/>
            <ui:select options="${currencies}" label="Currens" name="currency"/>
            <ui:select options="${types}" label="Type" name="type"/>
            <ui:error/>
            <ui:button title="Submit" type="submit"/>
        </form>
    </ui:popup>

    <ui:popup id="delete">
        <form id="delete-account" method="DELETE" action="/accounts/{id}">
            <h2>Are you sure you want to delete the client?</h2>
            <ui:error/>
            <ui:input name="id" type="hidden"/>
            <ui:button title="Submit" type="submit"/>
        </form>
    </ui:popup>

    <ui:popup id="transfer">
        <form id="transfer-account" method="POST" action="/transactions/transfer">
            <h2>Transfer money</h2>
            <ui:input name="accountId" type="hidden"/>
            <ui:input label="Amount" name="amount" type="number"/>
            <ui:input label="Recipient" name="recipientId" type="number"/>
            <ui:error/>
            <ui:button title="Submit" type="submit"/>
        </form>
    </ui:popup>

    <ui:popup id="withdraw">
        <form id="withdraw-account" method="POST" action="/accounts/{id}/withdraw">
            <h2>Withdraw money</h2>
            <ui:input name="id" type="hidden"/>
            <ui:input label="Amount" name="amount" type="number"/>
            <ui:error/>
            <ui:button title="Submit" type="submit"/>
        </form>
    </ui:popup>

    <ui:popup id="deposit">
        <form id="deposit-account" method="POST" action="/accounts/{id}/deposit">
            <h2>Deposit money</h2>
            <ui:input name="id" type="hidden"/>
            <ui:input label="Amount" name="amount" type="number"/>
            <ui:error/>
            <ui:button title="Submit" type="submit"/>
        </form>
    </ui:popup>
</section>
<component:transaction />
</body>
</html>