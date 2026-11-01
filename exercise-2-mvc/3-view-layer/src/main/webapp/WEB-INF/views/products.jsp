<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ include file="header.jspf" %>

<%-- fix the locale so numbers look the same whatever the browser language is --%>
<fmt:setLocale value="en_IN"/>

<h1>${pageTitle}</h1>

<c:choose>
    <c:when test="${empty products}">
        <p>No products available.</p>
    </c:when>
    <c:otherwise>
        <table>
            <tr>
                <th>#</th>
                <th>Name</th>
                <th>Price</th>
                <th>Availability</th>
            </tr>
            <c:forEach var="p" items="${products}" varStatus="row">
                <tr>
                    <td>${row.count}</td>
                    <%-- c:out escapes the text so product names can't inject HTML --%>
                    <td><c:out value="${p.name}"/></td>
                    <td>&#8377; <fmt:formatNumber value="${p.price}" pattern="#,##0.00"/></td>
                    <td>
                        <c:choose>
                            <c:when test="${p.stock == 0}">Out of stock</c:when>
                            <c:when test="${p.stock < 5}">Only ${p.stock} left</c:when>
                            <c:otherwise>In stock (${p.stock})</c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </c:forEach>
        </table>
        <p class="muted">${products.size()} products listed.</p>
    </c:otherwise>
</c:choose>

<%@ include file="footer.jspf" %>
