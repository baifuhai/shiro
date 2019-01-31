<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>

<!DOCTYPE html>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <h1>index</h1>
    Welcome: <b><shiro:principal></shiro:principal></b>

    <br/>
    <br/>

    <shiro:hasRole name="admin">
        <a href="/admin">admin</a>
    </shiro:hasRole>

    <shiro:hasRole name="user">
        <a href="/user">user</a>
    </shiro:hasRole>

    <a href="/test">test</a>

    <a href="/remember">remember</a>

    <br/>
    <br/>

    <a href="/logout">logout</a>
</body>
</html>
