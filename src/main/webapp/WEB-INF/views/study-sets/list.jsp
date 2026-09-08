<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/fragments/taglibs.jspf" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Study Sets | StudyBox</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="app-shell">
    <aside class="sidebar">
        <div class="brand">
            <div class="brand-mark">S</div>
            <span>StudyBox</span>
        </div>

        <nav class="sidebar-nav">
            <a class="nav-item" href="${pageContext.request.contextPath}/dashboard">Dashboard</a>
            <a class="nav-item active" href="${pageContext.request.contextPath}/study-sets">Study Sets</a>
            <a class="nav-item" href="${pageContext.request.contextPath}/study-sets/new">Tạo bộ mới</a>
        </nav>

        <div class="sidebar-footer">
            <div style="font-weight: 700;"><c:out value="${sessionScope.currentUser.fullName}"/></div>
            <div class="subtle" style="color: #cbd5e1;"><c:out value="${sessionScope.currentUser.email}"/></div>
            <div style="margin-top: 12px;">
                <a class="btn btn-secondary" href="${pageContext.request.contextPath}/logout">Đăng xuất</a>
            </div>
        </div>
    </aside>

    <main class="content">
        <div class="topbar">
            <div>
                <h1>Study Sets</h1>
                <p class="subtle">Quản lý toàn bộ bộ học tập của bạn ở một nơi.</p>
            </div>
            <div class="page-actions">
                <a class="btn btn-primary" href="${pageContext.request.contextPath}/study-sets/new">+ Study Set mới</a>
            </div>
        </div>

        <%@ include file="/WEB-INF/views/fragments/messages.jspf" %>

        <section class="card">
            <c:choose>
                <c:when test="${empty studySets}">
                    <div class="empty-state">
                        Chưa có Study Set nào. Hãy tạo bộ đầu tiên của bạn.
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="table-wrap">
                        <table>
                            <thead>
                            <tr>
                                <th>Tên bộ</th>
                                <th>Chủ đề</th>
                                <th>Trạng thái</th>
                                <th>Cập nhật</th>
                                <th>Thao tác</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach items="${studySets}" var="item">
                                <tr>
                                    <td>
                                        <div style="font-weight: 700;"><c:out value="${item.name}"/></div>
                                        <div class="subtle"><c:out value="${item.description}"/></div>
                                    </td>
                                    <td><c:out value="${item.subject}"/></td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${item.status eq 'ACTIVE'}">
                                                <span class="badge badge-success">Active</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge badge-neutral">Archived</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <fmt:formatDate value="${item.updatedAt}" pattern="dd/MM/yyyy HH:mm"/>
                                    </td>
                                    <td>
                                        <div class="actions">
                                            <a class="btn btn-secondary" href="${pageContext.request.contextPath}/study-sets/view?id=${item.id}">Xem</a>
                                            <a class="btn btn-secondary" href="${pageContext.request.contextPath}/study-sets/edit?id=${item.id}">Sửa</a>
                                            <form method="post" action="${pageContext.request.contextPath}/study-sets/delete" style="display: inline;">
                                                <input type="hidden" name="id" value="${item.id}">
                                                <button class="btn btn-danger" type="submit" data-confirm="Bạn có chắc muốn xóa Study Set này không?">Xóa</button>
                                            </form>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:otherwise>
            </c:choose>
        </section>
    </main>
</div>
<script src="${pageContext.request.contextPath}/js/app.js"></script>
</body>
</html>
