<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<jsp:include page="fragments/headTag.jsp"/>
<body>
<jsp:include page="fragments/bodyHeader.jsp"/>
<section>
    <jsp:useBean id="menuTo" type="edu.volkov.restmanager.to.MenuTo" scope="request"/>
    <h3><spring:message code="menu.menuManaging"/></h3>
    <h3><spring:message code="${menuTo.id == null ? 'common.create' : 'common.edit'}"/></h3>

    <form method="post" action="admin/menus">
        <input type="hidden" name="id" value="${menuTo.id}">
        <input type="hidden" name="restId" value="${menuTo.restId}">
        <dl>
            <dt><spring:message code="common.name"/>:</dt>
            <dd><input type="text" value="${menuTo.name}" name="name" required></dd>
        </dl>
        <dl>
            <dt><spring:message code="common.date"/>:</dt>
            <dd><input type="date" value="${menuTo.menuDate}" size=40 name="menuDate" required></dd>
        </dl>
        <dl>
            <dt><spring:message code="common.enable"/>:</dt>
            <dd>
                <a><input type="radio" value="true" name="enabled" required>On</a>
                <a><input type="radio" value="false" name="enabled" required>Off</a>
            </dd>
        </dl>
        <button type="submit"><spring:message code="common.save"/></button>
        <button onclick="window.history.back()" type="button"><spring:message code="common.cancel"/></button>
    </form>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
