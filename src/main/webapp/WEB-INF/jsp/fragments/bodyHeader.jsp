<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<header>
    <a href="${pageContext.request.contextPath}"><spring:message code="app.home"/></a> |
    <a href="restaurants"><spring:message code="app.restaurants"/></a> |
    <a href="restaurantsManaging"><spring:message code="app.restaurantManaging"/></a> |
    <a href="menus"><spring:message code="app.dishesManaging"/></a> |
    <a href="users"><spring:message code="app.userManaging"/></a> |
    <a href="contacts"><spring:message code="app.contacts"/></a>
    <a href="login"><spring:message code="app.loginMenu"/></a>
</header>