<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>${initParam.appTitle}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/style.css">
</head>
<body>
    <h1>${initParam.appTitle}</h1>

    <c:if test="${not empty error}">
        <p><b>${error}</b></p>
    </c:if>

    <form action="students" method="post">
        <input type="hidden" name="action" value="add">
        <label>Name <input type="text" name="name" required></label>
        <label>Email <input type="email" name="email" required></label>
        <label>Course
            <select name="course">
                <option>B.Tech CSE</option>
                <option>B.Tech ECE</option>
                <option>BCA</option>
                <option>MCA</option>
            </select>
        </label>
        <p><button type="submit">Register</button></p>
    </form>

    <h2>Registered students</h2>
    <c:choose>
        <c:when test="${empty students}">
            <p class="muted">Nobody has registered yet.</p>
        </c:when>
        <c:otherwise>
            <table>
                <tr><th>ID</th><th>Name</th><th>Email</th><th>Course</th><th></th></tr>
                <c:forEach var="s" items="${students}">
                    <tr>
                        <td>${s.id}</td>
                        <td><c:out value="${s.name}"/></td>
                        <td><c:out value="${s.email}"/></td>
                        <td><c:out value="${s.course}"/></td>
                        <td>
                            <form action="students" method="post" style="display:inline">
                                <input type="hidden" name="action" value="remove">
                                <input type="hidden" name="id" value="${s.id}">
                                <button type="submit">Remove</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </c:otherwise>
    </c:choose>

    <p class="muted"><a href="${pageContext.request.contextPath}/nothing-here">Open a missing page</a> (shows the 404 error page from web.xml)</p>
</body>
</html>
