# StudyBox

StudyBox là một web app tự học bằng bộ câu hỏi, xây theo hướng Java Servlet + JSP + MySQL, kiến trúc MVC rõ ràng.

## Tính năng Phase 1

- Đăng ký tài khoản
- Đăng nhập / đăng xuất
- Bảo vệ phiên đăng nhập bằng session
- Tạo, xem, sửa, xóa Study Set
- Dashboard cơ bản cho người dùng
- Hash mật khẩu bằng PBKDF2

## Tech Stack

- Frontend: HTML, CSS, JavaScript, JSP
- Backend: Java Servlet, MVC
- Database: MySQL
- Build tool: Maven
- Server: Apache Tomcat 9

## Cấu trúc dự án

```text
src/
  main/
    java/com/studybox/
      controller/
      dao/
      filter/
      model/
      service/
      util/
    resources/
    webapp/
      css/
      js/
      WEB-INF/views/
sql/
```

## Cài đặt

1. Tạo database bằng file `sql/schema.sql`
2. Cập nhật thông tin kết nối trong `src/main/resources/db.properties`
3. Build project bằng Maven
4. Deploy file WAR lên Tomcat

## Chạy dự án

- URL gốc: `/`
- Nếu chưa đăng nhập: chuyển sang trang đăng nhập
- Nếu đã đăng nhập: chuyển sang dashboard

## Ghi chú

- Phase 1 chỉ tập trung vào nền tảng, auth và CRUD Study Set.
- Kiến trúc đã tách sẵn để sau này nối thêm Question, Quiz, Flashcard, History và AI.
