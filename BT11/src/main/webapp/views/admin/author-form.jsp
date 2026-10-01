<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head><title>${empty author.authorId ? 'Thêm tác giả' : 'Sửa tác giả'}</title></head>
<body>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="isEdit" value="${not empty author.authorId}"/>

<h3 class="mb-3">${isEdit ? 'Sửa tác giả #'.concat(author.authorId) : 'Thêm tác giả mới'}</h3>

<c:if test="${not empty error}">
    <div class="alert alert-danger"><c:out value="${error}"/></div>
</c:if>

<form method="post" action="${ctx}/admin/authors/${isEdit ? 'edit' : 'add'}" class="card card-body" style="max-width: 560px">
    <c:if test="${isEdit}">
        <input type="hidden" name="authorId" value="${author.authorId}">
    </c:if>
    <div class="mb-3">
        <label class="form-label">Tên tác giả *</label>
        <input type="text" name="authorName" maxlength="100" class="form-control" required
               value="<c:out value='${author.authorName}'/>">
    </div>
    <div class="mb-3">
        <label class="form-label">Ngày sinh</label>
        <input type="date" name="dateOfBirth" class="form-control" value="${author.dateOfBirth}">
    </div>
    <div>
        <button type="submit" class="btn btn-primary">${isEdit ? 'Cập nhật' : 'Thêm mới'}</button>
        <a href="${ctx}/admin/authors" class="btn btn-secondary">Hủy</a>
    </div>
</form>
</body>
</html>
