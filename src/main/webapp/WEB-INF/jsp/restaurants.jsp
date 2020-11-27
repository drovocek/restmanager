<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<jsp:include page="fragments/headTag.jsp"/>
<body>
<jsp:include page="fragments/bodyHeader.jsp"/>
<section>
    <h3><spring:message code="restaurant.title"/></h3>
    <a href="restaurants/restaurantForm"><spring:message code="common.add"/></a>
    <table border="1" cellpadding="8" cellspacing="0">
        <thead>
        <tr>
            <th><spring:message code="common.name"/></th>
            <th><spring:message code="restaurant.address"/></th>
            <th><spring:message code="restaurant.phone"/></th>
            <th><spring:message code="restaurant.likesAmount"/></th>
            <th>Voting</th>
        </tr>
        </thead>
        <c:forEach items="${restaurants}" var="restaurant">
            <jsp:useBean id="restaurant" scope="page" type="edu.volkov.restmanager.to.RestaurantTo"/>
            <tr>
                <td><a href="restaurants/restaurant?id=${restaurant.id}">${restaurant.name}</a></td>
                <td>${restaurant.address}</td>
                <td>${restaurant.phone}</td>
                <td>${restaurant.likesAmount}</td>
                <td><a href="restaurants/vote?id=${restaurant.id}">Vote</a></td>
            </tr>
        </c:forEach>
    </table>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>