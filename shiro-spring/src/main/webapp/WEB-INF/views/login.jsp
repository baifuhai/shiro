<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <h1>login</h1>
    <form action="/login" method="post">
        <div>
            <input type="text" name="username"/>
        </div>
        <div>
            <input type="text" name="password"/>
        </div>
        <div>
            <input type="submit" value="login"/>
        </div>
    </form>
    <h3>${message}</h3>
</body>
</html>
