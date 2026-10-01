<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html>
<head><title>${empty book.bookid ? 'Thêm sách' : 'Sửa sách'}</title></head>
<body>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="isEdit" value="${not empty book.bookid}"/>

<h3 class="mb-3">${isEdit ? 'Sửa sách #'.concat(book.bookid) : 'Thêm sách mới'}</h3>

<c:if test="${not empty error}">
    <div class="alert alert-danger"><c:out value="${error}"/></div>
</c:if>

<form method="post" enctype="multipart/form-data"
      action="${ctx}/admin/books/${isEdit ? 'edit' : 'add'}" class="card card-body">
    <c:if test="${isEdit}">
        <input type="hidden" name="bookid" value="${book.bookid}">
    </c:if>
    <div class="row">
        <div class="col-md-8">
            <div class="mb-3">
                <label class="form-label">Tiêu đề *</label>
                <input type="text" name="title" maxlength="200" class="form-control" required
                       value="<c:out value='${book.title}'/>">
            </div>
            <div class="row">
                <div class="col-md-4 mb-3">
                    <label class="form-label">ISBN</label>
                    <input type="number" name="isbn" class="form-control" value="${book.isbn}">
                </div>
                <div class="col-md-4 mb-3">
                    <label class="form-label">Giá</label>
                    <input type="number" name="price" step="0.01" min="0" max="9999.99" class="form-control"
                           value="${book.price}">
                </div>
                <div class="col-md-4 mb-3">
                    <label class="form-label">Số lượng</label>
                    <input type="number" name="quantity" min="0" class="form-control" value="${book.quantity}">
                </div>
            </div>
            <div class="row">
                <div class="col-md-8 mb-3">
                    <label class="form-label">Publisher</label>
                    <input type="text" name="publisher" maxlength="100" class="form-control"
                           value="<c:out value='${book.publisher}'/>">
                </div>
                <div class="col-md-4 mb-3">
                    <label class="form-label">Ngày xuất bản</label>
                    <input type="date" name="publishDate" class="form-control" value="${book.publishDate}">
                </div>
            </div>
            <div class="mb-3">
                <label class="form-label">Mô tả</label>
                <textarea name="description" rows="4" class="form-control"><c:out value="${book.description}"/></textarea>
            </div>
            <div class="mb-3">
                <label class="form-label d-block">Tác giả</label>
                <c:forEach var="a" items="${authors}">
                    <div class="form-check form-check-inline">
                        <input class="form-check-input" type="checkbox" name="authorIds" id="a${a.authorId}"
                               value="${a.authorId}" ${selectedAuthorIds.contains(a.authorId) ? 'checked' : ''}>
                        <label class="form-check-label" for="a${a.authorId}"><c:out value="${a.authorName}"/></label>
                    </div>
                </c:forEach>
                <c:if test="${empty authors}">
                    <div class="text-secondary small">Chưa có tác giả — <a href="${ctx}/admin/authors/add">thêm tác giả</a>.</div>
                </c:if>
            </div>
        </div>
        <div class="col-md-4">
            <label class="form-label">Ảnh bìa</label>
            <div class="mb-2">
                <t:cover src="${book.coverImage}" alt="cover" cssClass="img-fluid rounded border"/>
            </div>
            <input type="file" name="coverFile" accept="image/*" class="form-control mb-2">
            <input type="text" name="coverUrl" maxlength="100" class="form-control" placeholder="hoặc dán link ảnh https://..."
                   value="${fn:startsWith(book.coverImage, 'http') ? fn:escapeXml(book.coverImage) : ''}">
            <div class="form-text">Để trống nếu giữ ảnh cũ.</div>
        </div>
    </div>
    <div class="mt-3">
        <button type="submit" class="btn btn-primary">${isEdit ? 'Cập nhật' : 'Thêm mới'}</button>
        <a href="${ctx}/admin/books" class="btn btn-secondary">Hủy</a>
    </div>
</form>
</body>
</html>
