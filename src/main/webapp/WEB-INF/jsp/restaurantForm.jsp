<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<jsp:include page="fragments/headTag.jsp"/>
<body>
<jsp:include page="fragments/bodyHeader.jsp"/>
<section>
    <jsp:useBean id="restTo" type="edu.volkov.restmanager.to.RestaurantTo" scope="request"/>
    <h3><spring:message code="restaurant.restaurantManaging"/></h3>
    <h3><spring:message code="${restTo.id == null ? 'common.create' : 'common.edit'}"/></h3>

    <form method="post" action="restaurantsManaging">
        <input type="hidden" name="id" value="${restTo.id}">
        <dl>
            <dt><spring:message code="common.name"/>:</dt>
            <dd><input type="text" value="${restTo.name}" name="name" required></dd>
        </dl>
        <dl>
            <dt><spring:message code="restaurant.address"/>:</dt>
            <dd><input type="text" value="${restTo.address}" size=40 name="address" required></dd>
        </dl>
        <dl>
            <dt><spring:message code="restaurant.phone"/><br/>(+n (nnn) nnn-nnnn):</dt>
            <dd><input type="text" value="${restTo.phone}" name="phone" required></dd>
        </dl>
        <dl>
            <dt><spring:message code="restaurant.enabled"/>:</dt>
            <dd><a><input type="radio" value="true" name="enabled" required>On</a>
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
