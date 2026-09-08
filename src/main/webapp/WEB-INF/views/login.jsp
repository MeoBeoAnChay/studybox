<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/fragments/taglibs.jspf" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng nhập | StudyBox</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="page-shell auth-shell">
<div class="auth-card">
    <div class="brand">
        <div class="brand-mark">S</div>
        <span>StudyBox</span>
    </div>

    <div class="hero-copy">
        <h1>Đăng nhập để tiếp tục học</h1>
        <p>Quản lý Study Set, học lại câu sai và giữ nhịp tự học mỗi ngày.</p>
    </div>

    <%@ include file="/WEB-INF/views/fragments/messages.jspf" %>

    <form method="post" action="${pageContext.request.contextPath}/login">
        <div class="field">
            <label for="email">Email</label>
            <input class="input" type="email" id="email" name="email" placeholder="you@example.com"
                   value="${fn:escapeXml(email)}" required>
        </div>

        <div class="field">
            <label for="password">Mật khẩu</label>
            <input class="input" type="password" id="password" name="password" placeholder="Nhập mật khẩu" required>
        </div>

        <button class="btn btn-primary" type="submit" style="width: 100%;">Đăng nhập</button>
    </form>

    <div class="help-row">
        <span>Chưa có tài khoản?</span>
        <a class="btn-link" href="${pageContext.request.contextPath}/register">Tạo tài khoản mới</a>
    </div>
</div>
</body>
</html>
