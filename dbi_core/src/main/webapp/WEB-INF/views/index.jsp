<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<%@ taglib prefix="ui" tagdir="/WEB-INF/tags/UI"%>
<%@ taglib prefix="component" tagdir="/WEB-INF/tags/components"%>

<!DOCTYPE html>
<html>
<tag:header title="DBI" />
<body>
    <component:client />
    <component:accounts currencies="${currencies}"/>
    <component:transaction />
</body>
</html>