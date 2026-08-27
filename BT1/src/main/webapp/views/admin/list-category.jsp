<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html><head><meta charset="UTF-8"><title>Danh sach Category</title></head>
<body>
<h2>Danh sach Category</h2>
<a href="<c:url value='/admin/category/add'/>">+ Them danh muc</a>
<table border="1" cellpadding="6" cellspacing="0">
<tr><th>STT</th><th>Anh</th><th>Ten</th><th>Thao tac</th></tr>
<c:forEach items="${cateList}" var="cate" varStatus="stt">
<tr>
    <td>${stt.index + 1}</td>
    <td>
        <c:if test="${not empty cate.icon}">
            <c:url value="/image?fname=${cate.icon}" var="imgUrl"/>
            <img height="60" src="${imgUrl}"/>
        </c:if>
    </td>
    <td>${cate.name}</td>
    <td>
        <a href="<c:url value='/admin/category/edit?id=${cate.id}'/>">Sua</a> |
        <a href="<c:url value='/admin/category/delete?id=${cate.id}'/>"
           onclick="return confirm('Xoa danh muc nay?')">Xoa</a>
    </td>
</tr>
</c:forEach>
</table>
</body></html>
