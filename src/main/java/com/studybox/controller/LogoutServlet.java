package com.studybox.controller;

import com.studybox.util.FlashUtil;
import com.studybox.util.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logout(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logout(request, response);
    }

    private void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getSession(false) != null) {
            SessionUtil.clearCurrentUser(request.getSession(false));
            request.getSession(false).invalidate();
        }
        FlashUtil.success(request.getSession(true), "Bạn đã đăng xuất.");
        response.sendRedirect(request.getContextPath() + "/login");
    }
}
