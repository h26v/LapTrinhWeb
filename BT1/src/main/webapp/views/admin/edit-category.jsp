<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html><head><meta charset="UTF-8"><title>Sua Category</title></head>
<body>
<h2>Sua danh muc</h2>
<c:url value="/admin/category/edit" var="editUrl"/>
<form action="${editUrl}" method="post" enctype="multipart/form-data">
    <input type="hidden" name="id" value="${category.id}"/>
    <p>Ten danh muc: <input type="text" name="name" value="${category.name}" required/></p>
    <p>
        <c:if test="${not empty category.icon}">
            <c:url value="/image?fname=${category.icon}" var="imgUrl"/>
            <img width="100" src="${imgUrl}"/><br/>
        </c:if>
        Doi anh: <input type="file" name="icon"/>
    </p>
    <button type="submit">Cap nhat</button>
    <a href="list">Quay lai</a>
</form>
</body></html>
