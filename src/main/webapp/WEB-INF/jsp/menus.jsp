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
    <a href="admin/menus/create?id=&restId=0"><spring:message code="common.add"/></a>
    <table border="1" cellpadding="8" cellspacing="0">
        <thead>
        <tr>
            <th><spring:message code="common.name"/></th>
            <th><spring:message code="common.date"/></th>
            <th><spring:message code="common.enable"/></th>
            <th><spring:message code="menu.dishes"/></th>
            <th></th>
            <th></th>
        </tr>
        </thead>
        <c:forEach items="${menuTos}" var="menuTo">
            <jsp:useBean id="menuTo" scope="page" type="edu.volkov.restmanager.to.MenuTo"/>
            <tr>
                <td>${menuTo.name}</td>
                <td>${menuTo.menuDate}</td>
                <td>${menuTo.enabled}</td>
                <td>Empty</td>
                <td><a href="admin/menus/update?id=${menuTo.id}&restId=${menuTo.restId}"><spring:message
                        code="common.update"/></a>
                </td>
                <td><a href="admin/menus/delete?id=${menuTo.id}&restId=${menuTo.restId}"><spring:message
                        code="common.delete"/></a></td>
            </tr>
        </c:forEach>
    </table>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>