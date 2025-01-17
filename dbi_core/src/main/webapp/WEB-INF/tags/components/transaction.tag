<%@ taglib prefix="ui" tagdir="/WEB-INF/tags/UI" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<section class="transaction">
    <div class="transaction-wrapper">
        <div class="transaction-content">
            <h2><spring:message code="message.transaction.title" /></h2>

            <div class="transaction-info-wrapper">
                <p><spring:message code="message.transaction.info" /></p>
                <div class="transaction-info">
                    <div class="loader"></div>
                    <div class="ref none">
                        <div class="transaction-element">

                            <div>
                                <p id="date"></p>
                                <p id="time"></p>
                            </div>
                            <div>
                                <p><spring:message code="message.form.type" />:</p>
                                <p id="type"></p>
                            </div>
                            <div>
                                <p><spring:message code="message.form.amount" />:</p>
                                <p id="amount"></p>
                            </div>
                            <div class="transaction-account-wrapper">
                                <p><spring:message code="message.account.title" />:</p>
                                <div id="transaction-account-element">
                                    <p id="name"><spring:message code="message.form.name" />: <span></span> </p>
                                    <p id="currency"><spring:message code="message.form.currency" />: <span></span></p>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="info"></div>
                </div>
            </div>
        </div>

    </div>
</section>
