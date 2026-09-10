<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%-- Dieu huong nhanh giua cac bang quan tri. --%>
<ul class="nav nav-pills gap-2 mb-3">
    <li class="nav-item">
        <a class="nav-link ${adminTab == 'category' ? 'active' : 'link-secondary'}"
           href="<c:url value='/admin/categories'/>">Category</a>
    </li>
    <li class="nav-item">
        <a class="nav-link ${adminTab == 'user' ? 'active' : 'link-secondary'}"
           href="<c:url value='/admin/users'/>">User</a>
    </li>
    <li class="nav-item">
        <a class="nav-link ${adminTab == 'product' ? 'active' : 'link-secondary'}"
           href="<c:url value='/admin/products'/>">Product</a>
    </li>
</ul>
