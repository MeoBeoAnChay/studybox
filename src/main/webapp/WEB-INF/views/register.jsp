<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/fragments/taglibs.jspf" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng ký | StudyBox</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="page-shell auth-shell">
<div class="auth-card">
    <div class="brand">
        <div class="brand-mark">S</div>
        <span>StudyBox</span>
    </div>

    <div class="hero-copy">
        <h1>Tạo tài khoản StudyBox</h1>
        <p>Bắt đầu xây bộ câu hỏi của riêng bạn và học lại theo nhiều chế độ khác nhau.</p>
    </div>

    <%@ include file="/WEB-INF/views/fragments/messages.jspf" %>

    <form method="post" action="${pageContext.request.contextPath}/register">
        <div class="field">
            <label for="fullName">Họ và tên</label>
            <input class="input" type="text" id="fullName" name="fullName" placeholder="Nguyễn Văn A"
                   value="${fn:escapeXml(fullName)}" required>
        </div>

        <div class="field">
            <label for="email">Email</label>
            <input class="input" type="email" id="email" name="email" placeholder="you@example.com"
                   value="${fn:escapeXml(email)}" required>
        </div>

        <div class="field">
            <label for="password">Mật khẩu</label>
            <input class="input" type="password" id="password" name="password" placeholder="Ít nhất 8 ký tự" required>
        </div>

        <div class="field">
            <label for="confirmPassword">Xác nhận mật khẩu</label>
            <input class="input" type="password" id="confirmPassword" name="confirmPassword" required>
        </div>

        <button class="btn btn-primary" type="submit" style="width: 100%;">Tạo tài khoản</button>
    </form>

    <div class="help-row">
        <span>Đã có tài khoản?</span>
        <a class="btn-link" href="${pageContext.request.contextPath}/login">Đăng nhập</a>
    </div>
</div>
</body>
</html>
