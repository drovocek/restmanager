<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<jsp:include page="fragments/headTag.jsp"/>
<body>
<jsp:include page="fragments/bodyHeader.jsp"/>
<section>
    <h3><spring:message code="menu.menuManaging"/></h3>
    <spring:message code="common.create" var="createMenu"/>
    <spring:message code="common.edit" var="editMenu"/>
    <h2>${restaurant.id == null ? createMenu : editMenu}</h2>
    <jsp:useBean id="menu" type="edu.volkov.restmanager.model.Menu" scope="request"/>

    <form method="post" action="menus">
        <input type="hidden" name="id" value="${menu.id}">
        <dl>
            <dt><spring:message code="common.name"/>:</dt>
            <dd><input type="text" value="${menu.name}" name="name" required></dd>
        </dl>
        <dl>
            <dt><spring:message code="common.date"/>:</dt>
            <dd><input type="date" value="${menu.menuDate}" size=40 name="address" required></dd>
        </dl>
        <dl>
            <dt><spring:message code="common.enable"/><br />:</dt>
            <dd><input type="checkbox" value="${menu.enabled}" name="phone" required></dd>
        </dl>
        <button type="submit"><spring:message code="common.save"/></button>
        <button onclick="window.history.back()" type="button"><spring:message code="common.cancel"/></button>
    </form>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
