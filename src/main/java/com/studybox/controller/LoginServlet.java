package com.studybox.controller;

import com.studybox.exception.ValidationException;
import com.studybox.model.User;
import com.studybox.service.UserService;
import com.studybox.util.FlashUtil;
import com.studybox.util.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (SessionUtil.getCurrentUser(request) != null) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try {
            User user = userService.authenticate(email, password);
            SessionUtil.setCurrentUser(request.getSession(true), user);
            FlashUtil.success(request.getSession(), "Đăng nhập thành công.");
            response.sendRedirect(request.getContextPath() + "/dashboard");
        } catch (ValidationException ex) {
            request.setAttribute("errors", ex.getErrors());
            request.setAttribute("email", email == null ? "" : email.trim());
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        }
    }
}
