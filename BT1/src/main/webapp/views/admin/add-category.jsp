<%@ page pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html><head><meta charset="UTF-8"><title>Them Category</title></head>
<body>
<h2>Them danh muc</h2>
<form action="add" method="post" enctype="multipart/form-data">
    <p>Ten danh muc: <input type="text" name="name" required/></p>
    <p>Anh dai dien: <input type="file" name="icon"/></p>
    <button type="submit">Them</button>
    <a href="list">Quay lai</a>
</form>
</body></html>
