<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><sitemesh:write property="title">BT3 - User Profile</sitemesh:write></title>
    <sitemesh:write property="head" />
    <style>
        :root { color-scheme: light; font-family: Arial, sans-serif; }
        * { box-sizing: border-box; }
        body { margin: 0; background: #f4f7fb; color: #172033; }
        .topbar { background: #172033; color: #fff; }
        .nav { max-width: 960px; margin: 0 auto; padding: 16px 24px; display: flex;
               align-items: center; justify-content: space-between; gap: 20px; }
        .brand { color: #fff; font-weight: 700; text-decoration: none; letter-spacing: .04em; }
        .nav-links { display: flex; gap: 16px; flex-wrap: wrap; }
        .nav-links a { color: #d9e4f3; text-decoration: none; }
        .nav-links a:hover { color: #fff; }
        main { max-width: 960px; margin: 36px auto; padding: 0 24px; min-height: calc(100vh - 150px); }
        .card { background: #fff; border: 1px solid #dce4ef; border-radius: 12px; padding: 28px;
                box-shadow: 0 8px 24px rgba(23, 32, 51, .06); }
        .footer { max-width: 960px; margin: 0 auto; padding: 20px 24px 32px; color: #69758a; font-size: .9rem; }
    </style>
</head>
<body>
    <header class="topbar">
        <nav class="nav">
            <a class="brand" href="<%= request.getContextPath() %>/">BT3 · PROFILE</a>
            <div class="nav-links">
                <a href="<%= request.getContextPath() %>/">Trang chủ</a>
                <a href="<%= request.getContextPath() %>/session/profile">Profile</a>
                <a href="<%= request.getContextPath() %>/session/logout">Đăng xuất</a>
            </div>
        </nav>
    </header>
    <main>
        <sitemesh:write property="body">Nội dung đang được tải.</sitemesh:write>
    </main>
    <footer class="footer">BT3 · JPA / Hibernate · SiteMesh</footer>
</body>
</html>
