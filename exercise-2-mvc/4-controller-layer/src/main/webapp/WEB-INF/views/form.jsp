<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>${task.id == 0 ? "New task" : "Edit task"}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/style.css">
</head>
<body>
    <h1>${task.id == 0 ? "New task" : "Edit task"}</h1>

    <c:if test="${not empty error}">
        <p><b>${error}</b></p>
    </c:if>

    <form action="tasks" method="post">
        <input type="hidden" name="action" value="save">
        <input type="hidden" name="id" value="${task.id}">
        <label>Title
            <input type="text" name="title" value="<c:out value='${task.title}'/>" size="40" autofocus>
        </label>
        <p><button type="submit">Save</button> <a href="tasks">Cancel</a></p>
    </form>
</body>
</html>
