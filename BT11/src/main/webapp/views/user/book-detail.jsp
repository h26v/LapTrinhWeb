<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html>
<head><title><c:out value="${book.title}"/></title></head>
<body>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<nav class="mb-3"><a href="${ctx}/home">&laquo; Quay lại danh sách</a></nav>

<table class="table table-bordered bg-white align-top">
    <tr>
        <%-- [cover_image] --%>
        <td style="width: 260px">
            <t:cover src="${book.coverImage}" alt="${book.title}" cssClass="img-fluid rounded book-cover"/>
        </td>
        <td>
            <h4 class="mb-3"><c:out value="${book.title}"/></h4>
            <p class="mb-1"><strong>Tiêu đề:</strong> <c:out value="${book.title}"/></p>
            <p class="mb-1"><strong>Mã isbn:</strong> ${book.isbn}</p>
            <p class="mb-1"><strong>Tác giả:</strong> <c:out value="${book.authorNames}"/></p>
            <p class="mb-1"><strong>Publisher:</strong> <c:out value="${book.publisher}"/></p>
            <p class="mb-1"><strong>Publisher_date:</strong> ${book.publishDate}</p>
            <p class="mb-1"><strong>Quantity:</strong> ${book.quantity}</p>
            <p class="mb-1"><strong>Price:</strong> ${book.price}</p>
            <p class="mb-1"><a href="#reviews">Reviews (${book.reviewCount})</a></p>

            <%-- Them vao gio hang --%>
            <div class="mt-3">
                <c:choose>
                    <c:when test="${not book.inStock}">
                        <span class="badge text-bg-secondary fs-6">Hết hàng</span>
                    </c:when>
                    <c:when test="${empty sessionScope.account}">
                        <c:url var="loginUrl" value="/login"><c:param name="next" value="/book/detail?id=${book.bookid}"/></c:url>
                        <a href="${loginUrl}" class="btn btn-outline-success"><i class="bi bi-cart-plus"></i> Đăng nhập để mua</a>
                    </c:when>
                    <c:otherwise>
                        <form method="post" action="${ctx}/cart/add" class="d-flex flex-wrap align-items-center gap-2">
                            <input type="hidden" name="bookid" value="${book.bookid}">
                            <input type="hidden" name="back" value="/book/detail?id=${book.bookid}">
                            <input type="number" name="quantity" value="1" min="1" max="${book.maxOrderQuantity}"
                                   class="form-control" style="width: 90px">
                            <button class="btn btn-success"><i class="bi bi-cart-plus"></i> Thêm vào giỏ</button>
                            <span class="small text-secondary">Tối đa ${book.maxOrderQuantity} cuốn</span>
                        </form>
                    </c:otherwise>
                </c:choose>
            </div>
            <c:if test="${not empty book.description}">
                <p class="mt-3 text-secondary"><c:out value="${book.description}"/></p>
            </c:if>
        </td>
    </tr>
    <tr id="reviews">
        <th colspan="2" class="table-light">Reviews</th>
    </tr>
    <tr>
        <td colspan="2">
            <c:if test="${empty reviews}">
                <em class="text-secondary">Chưa có review nào.</em>
            </c:if>
            <c:forEach var="r" items="${reviews}">
                <div class="border-bottom py-2">
                    <strong><c:out value="${r.user.fullname}"/></strong>:
                    <c:out value="${r.reviewText}"/>
                    <c:if test="${r.rating != null}">
                        <span class="text-warning ms-2">
                            <c:forEach begin="1" end="${r.rating}">&#9733;</c:forEach>
                        </span>
                    </c:if>
                </div>
            </c:forEach>
        </td>
    </tr>
    <tr>
        <td colspan="2">
            <h6>Form thêm reviews</h6>
            <c:if test="${not empty sessionScope.flash}">
                <div class="alert alert-success py-2"><c:out value="${sessionScope.flash}"/></div>
                <c:remove var="flash" scope="session"/>
            </c:if>
            <c:if test="${not empty sessionScope.flashError}">
                <div class="alert alert-danger py-2"><c:out value="${sessionScope.flashError}"/></div>
                <c:remove var="flashError" scope="session"/>
            </c:if>
            <c:choose>
                <c:when test="${not empty sessionScope.account}">
                    <form method="post" action="${ctx}/book/review">
                        <input type="hidden" name="bookid" value="${book.bookid}">
                        <div class="mb-2" style="max-width: 200px">
                            <label class="form-label">Đánh giá</label>
                            <select name="rating" class="form-select">
                                <option value="5">5 &#9733;</option>
                                <option value="4">4 &#9733;</option>
                                <option value="3">3 &#9733;</option>
                                <option value="2">2 &#9733;</option>
                                <option value="1">1 &#9733;</option>
                            </select>
                        </div>
                        <div class="mb-2">
                            <textarea name="reviewText" rows="3" class="form-control" required
                                      placeholder="Nhận xét của bạn..."></textarea>
                        </div>
                        <button type="submit" class="btn btn-primary">Submit</button>
                    </form>
                </c:when>
                <c:otherwise>
                    <p>Vui lòng <a href="${ctx}/login">đăng nhập</a> để viết review.</p>
                </c:otherwise>
            </c:choose>
        </td>
    </tr>
</table>
</body>
</html>
