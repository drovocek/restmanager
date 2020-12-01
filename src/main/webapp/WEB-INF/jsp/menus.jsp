<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<jsp:include page="fragments/headTag.jsp"/>
<body>
<jsp:include page="fragments/bodyHeader.jsp"/>
<section>
    <h3><spring:message code="menu.title"/></h3>
    <a href="menus/menuForm?id=&restId=0"><spring:message code="common.add"/></a>
    <table border="1" cellpadding="8" cellspacing="0">
        <thead>
        <tr>
            <th><spring:message code="common.name"/></th>
            <th><spring:message code="common.date"/></th>
            <th><spring:message code="common.enable"/></th>
            <th><spring:message code="menu.dishes"/></th>
            <th></th>
            <th></th>
            <%--            <th>Restaurant</th>--%>
            <%--            <th>Restaurant popularity</th>--%>
        </tr>
        </thead>
        <c:forEach items="${menus}" var="menu">
            <jsp:useBean id="menu" scope="page" type="edu.volkov.restmanager.model.Menu"/>
            <tr>
                <td>${menu.name}</td>
                <td>${menu.menuDate}</td>
                <td>${menu.enabled}</td>
                <td>Empty</td>
                    <%--                <td>${menu.restaurant}</td>--%>
                    <%--                <td>${menu.restaurant.votesQuantity}</td>--%>
                <td><a href="menus/menuForm?id=${menu.id}&restId=${menu.restaurant.id}"><spring:message
                        code="common.update"/></a>
                </td>
                <td><a href="menus/delete?id=${menu.id}&restId=${menu.restaurant.id}"><spring:message
                        code="common.delete"/></a></td>
            </tr>
        </c:forEach>
    </table>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>