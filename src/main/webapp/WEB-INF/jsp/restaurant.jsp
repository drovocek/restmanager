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
    <jsp:useBean id="restTo" type="edu.volkov.restmanager.to.RestaurantTo" scope="request"/>

    <dl>
        <dt><spring:message code="common.name"/>:</dt>
        <dd>${restTo.name}</dd>
    </dl>
    <dl>
        <dt><spring:message code="restaurant.address"/>:</dt>
        <dd>${restTo.address}</dd>
    </dl>
    <dl>
        <dt><spring:message code="restaurant.phone"/>:</dt>
        <dd>${restTo.phone}</dd>
    </dl>
    <dl>
        <dt><spring:message code="restaurant.likesAmount"/>:</dt>
        <dd>${restTo.likesAmount}</dd>
    </dl>
    <dl>
        <dt><spring:message code="restaurant.menu"/>:</dt>
        <dd>
            <c:forEach items="${restTo.menus}" var="menu">
                <h4>${menu.name}:</h4>
            </c:forEach>
        </dd>
    </dl>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>