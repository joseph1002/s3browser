package com.cos.web.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by TQ3A016 on 2/26/2015.
 */
public class LoginFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(LoginFilter.class);

    private String[] excludedUrls;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String excludes = filterConfig.getInitParameter("excludedUrls");
        logger.debug("excludes=" + excludes);

        if (excludes != null) {
            this.excludedUrls = excludes.split(",");
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String requestUri = httpRequest.getRequestURI();
        logger.debug("doFilter for " + requestUri);

        for (String url : excludedUrls) {
            if (requestUri.contains(url.trim())) {
                logger.debug("exclude");
                chain.doFilter(request, response);
                return;
            }
        }
        // intercept
        // getSession(true) is required, otherwise there will null pointer at next line.
        HttpSession session = httpRequest.getSession(true);
        if (session.getAttribute("subscriber") == null) {
            logger.debug("user should login first. ");
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
        } else {
            chain.doFilter(request, response);
            return;
        }
    }

    @Override
    public void destroy() {

    }
}
