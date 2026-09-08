package com.studybox.service;

import com.studybox.dao.UserDao;
import com.studybox.exception.ValidationException;
import com.studybox.model.User;
import com.studybox.util.PasswordUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class UserService {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$", Pattern.CASE_INSENSITIVE);
    private final UserDao userDao;

    public UserService() {
        this(new UserDao());
    }

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User register(String fullName, String email, String password, String confirmPassword) {
        String normalizedFullName = normalize(fullName);
        String normalizedEmail = normalizeEmail(email);

        List<String> errors = new ArrayList<>();
        if (normalizedFullName.isEmpty()) {
            errors.add("Vui lòng nhập họ và tên.");
        }
        if (normalizedFullName.length() > 120) {
            errors.add("Họ và tên không được vượt quá 120 ký tự.");
        }
        if (normalizedEmail.isEmpty()) {
            errors.add("Vui lòng nhập email.");
        } else if (!EMAIL_PATTERN.matcher(normalizedEmail).matches()) {
            errors.add("Email không hợp lệ.");
        }
        if (password == null || password.isBlank()) {
            errors.add("Vui lòng nhập mật khẩu.");
        } else if (password.length() < 8) {
            errors.add("Mật khẩu phải có ít nhất 8 ký tự.");
        }
        if (confirmPassword == null || confirmPassword.isBlank()) {
            errors.add("Vui lòng xác nhận mật khẩu.");
        } else if (password == null || !password.equals(confirmPassword)) {
            errors.add("Mật khẩu xác nhận không khớp.");
        }
        if (!normalizedEmail.isEmpty() && userDao.findByEmail(normalizedEmail).isPresent()) {
            errors.add("Email đã được sử dụng.");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        User user = new User();
        user.setFullName(normalizedFullName);
        user.setEmail(normalizedEmail);
        user.setPasswordHash(PasswordUtil.hashPassword(password));
        user.setRole("USER");
        user.setStatus("ACTIVE");
        userDao.create(user);
        return user;
    }

    public User authenticate(String email, String password) {
        String normalizedEmail = normalizeEmail(email);
        List<String> errors = new ArrayList<>();

        if (normalizedEmail.isEmpty()) {
            errors.add("Vui lòng nhập email.");
        }
        if (password == null || password.isBlank()) {
            errors.add("Vui lòng nhập mật khẩu.");
        }
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        User user = userDao.findByEmail(normalizedEmail)
                .orElseThrow(() -> new ValidationException("Email hoặc mật khẩu không đúng."));

        if (!"ACTIVE".equalsIgnoreCase(user.getStatus())) {
            throw new ValidationException("Tài khoản của bạn đang bị khóa.");
        }
        if (!PasswordUtil.matches(password, user.getPasswordHash())) {
            throw new ValidationException("Email hoặc mật khẩu không đúng.");
        }

        return user;
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim();
    }

    private String normalizeEmail(String value) {
        return normalize(value).toLowerCase(Locale.ROOT);
    }
}
