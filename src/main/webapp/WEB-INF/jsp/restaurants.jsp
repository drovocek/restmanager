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
        <c:forEach items="${restTos}" var="restTo">
            <jsp:useBean id="restTo" scope="page" type="edu.volkov.restmanager.to.RestaurantTo"/>
            <tr>
                <td><a href="restaurants/restaurant?id=${restTo.id}">${restTo.name}</a></td>
                <td>${restTo.address}</td>
                <td>${restTo.phone}</td>
                <td>${restTo.likesAmount}</td>
                <td><a href="restaurants/vote?id=${restTo.id}">Vote</a></td>
            </tr>
        </c:forEach>
    </table>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>