<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<jsp:include page="fragments/headTag.jsp"/>
<body>
<jsp:include page="fragments/bodyHeader.jsp"/>
<section>
    <h3><spring:message code="restaurant.restaurantManaging"/></h3>
    <spring:message code="common.create" var="createRestaurant"/>
    <spring:message code="common.edit" var="editRestaurant"/>
    <h2>${restaurant.id == null ? createRestaurant : editRestaurant}</h2>
    <jsp:useBean id="restaurant" type="edu.volkov.restmanager.model.Restaurant" scope="request"/>

    <form method="post" action="restaurants">
        <input type="hidden" name="id" value="${restaurant.id}">
        <dl>
            <dt><spring:message code="common.name"/>:</dt>
            <dd><input type="text" value="${restaurant.name}" name="name" required></dd>
        </dl>
        <dl>
            <dt><spring:message code="restaurant.address"/>:</dt>
            <dd><input type="text" value="${restaurant.address}" size=40 name="address" required></dd>
        </dl>
        <dl>
            <dt><spring:message code="restaurant.phone"/><br />(+n (nnn) nnn-nnnn):</dt>
            <dd><input type="text" value="${restaurant.phone}" name="phone" required></dd>
        </dl>
        <button type="submit"><spring:message code="common.save"/></button>
        <button onclick="window.history.back()" type="button"><spring:message code="common.cancel"/></button>
    </form>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
