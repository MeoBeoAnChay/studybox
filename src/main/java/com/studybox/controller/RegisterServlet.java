package com.studybox.controller;

import com.studybox.exception.ValidationException;
import com.studybox.model.User;
import com.studybox.service.UserService;
import com.studybox.util.FlashUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        try {
            User user = userService.register(fullName, email, password, confirmPassword);
            FlashUtil.success(request.getSession(), "Đã tạo tài khoản cho " + user.getFullName() + ". Hãy đăng nhập.");
            response.sendRedirect(request.getContextPath() + "/login");
        } catch (ValidationException ex) {
            request.setAttribute("errors", ex.getErrors());
            request.setAttribute("fullName", fullName == null ? "" : fullName.trim());
            request.setAttribute("email", email == null ? "" : email.trim());
            request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
        }
    }
}
