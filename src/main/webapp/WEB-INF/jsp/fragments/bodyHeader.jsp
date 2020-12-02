<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<header>
    <a href="${pageContext.request.contextPath}"><spring:message code="app.home"/></a> |
    <a href="restaurants"><spring:message code="app.restaurants"/></a> |
    <a href="admin/restaurants"><spring:message code="app.restaurantManaging"/></a> |
    <a href="admin/menus"><spring:message code="app.dishesManaging"/></a> |
    <a href="admin/users"><spring:message code="app.userManaging"/></a> |
    <a href="login"><spring:message code="app.loginMenu"/></a>
</header>