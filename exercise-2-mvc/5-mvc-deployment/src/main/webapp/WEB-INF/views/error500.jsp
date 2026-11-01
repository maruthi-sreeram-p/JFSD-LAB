<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Something went wrong</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/style.css">
</head>
<body>
    <h1>Something went wrong</h1>
    <p>The request could not be completed. The details are in the server log.</p>
    <p><a href="${pageContext.request.contextPath}/students">Back to the application</a></p>
</body>
</html>
