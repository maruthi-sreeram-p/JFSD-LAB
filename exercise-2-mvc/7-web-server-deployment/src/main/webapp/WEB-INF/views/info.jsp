<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Deployed on a web server</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/style.css">
</head>
<body>
    <h1>The application is deployed</h1>
    <p>If you can read this page, the WAR file was unpacked and started by the web server.</p>

    <table>
        <tr><th>Web server</th><td><c:out value="${info.serverName}"/></td></tr>
        <tr><th>Servlet API</th><td>${info.servletApi}</td></tr>
        <tr><th>Context path</th><td><c:out value="${info.contextPath}"/></td></tr>
        <tr><th>Unpacked in</th><td><c:out value="${info.realPath}"/></td></tr>
        <tr><th>Java</th><td>${info.javaVersion}</td></tr>
        <tr><th>Operating system</th><td>${info.osName}</td></tr>
        <tr><th>Server time</th><td>${info.time}</td></tr>
    </table>
</body>
</html>
