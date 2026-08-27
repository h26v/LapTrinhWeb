<%@ page pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html><head><meta charset="UTF-8"><title>Login Session</title></head>
<body>
<h2>Login voi Session</h2>
<p style="color:red">${alert}</p>
<form action="login" method="post">
    <p>Username: <input type="text" name="username" required/></p>
    <p>Password: <input type="password" name="password" required/></p>
    <button type="submit">Login</button>
</form>
<p>(Tai khoan demo: trung / 123)</p>
</body></html>
