<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Tasks</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/style.css">
</head>
<body>
    <h1>Tasks</h1>
    <p><a href="tasks?action=new">Add task</a></p>

    <table>
        <tr><th>#</th><th>Title</th><th>Status</th><th></th></tr>
        <c:forEach var="t" items="${tasks}">
            <tr>
                <td>${t.id}</td>
                <td><c:out value="${t.title}"/></td>
                <td>${t.done ? "Done" : "Pending"}</td>
                <td>
                    <a href="tasks?action=edit&id=${t.id}">Edit</a>
                    <form action="tasks" method="post" style="display:inline">
                        <input type="hidden" name="action" value="toggle">
                        <input type="hidden" name="id" value="${t.id}">
                        <button type="submit">${t.done ? "Reopen" : "Complete"}</button>
                    </form>
                    <form action="tasks" method="post" style="display:inline">
                        <input type="hidden" name="action" value="delete">
                        <input type="hidden" name="id" value="${t.id}">
                        <button type="submit">Delete</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>
