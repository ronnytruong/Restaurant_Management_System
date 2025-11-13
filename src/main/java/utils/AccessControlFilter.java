package utils;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "AccessControlFilter", urlPatterns = {"/*"})
public class AccessControlFilter implements Filter {

    private static final String[] RESTRICTED_PATHS = {
        "/dashboard", "/orderItem", "/order", "/reservation", "/table",
        "/category", "/menuItem", "/type", "/ingredient", "/recipe",
        "/import", "/supplier", "/employee", "/customer", "/role", "/voucher"
    };

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String path = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());

        boolean isRestricted = false;
        for (String restrictedPath : RESTRICTED_PATHS) {
            if (path.equalsIgnoreCase(restrictedPath)) {
                isRestricted = true;
                break;
            }
        }

        if (isRestricted) {
            HttpSession session = httpRequest.getSession(false);

            if (session == null) {
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/homepage");
                return;
            }
            Object customerSession = session.getAttribute("customerSession");

            if (customerSession != null) {
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/homepage");
                return;
            }
        }
        chain.doFilter(request, response);
    } 

    @Override
    public void destroy() {

    }
}
