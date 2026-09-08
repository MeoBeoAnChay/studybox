package com.studybox.filter;

import com.studybox.util.FlashUtil;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter("/*")
public class FlashFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
        // No-op
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        FlashUtil.transfer((javax.servlet.http.HttpServletRequest) request);
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // No-op
    }
}
