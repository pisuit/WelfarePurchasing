package welfare.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import welfare.security.SecurityUser;
import welfare.utils.Constants;

public class AuthenticationFilter implements Filter {
	private FilterConfig filterConfig = null;
	private ServletContext servletContext = null;
	
	public void init(FilterConfig aFilterConfig) throws ServletException {
		filterConfig = aFilterConfig;
		servletContext = filterConfig.getServletContext();
	}
	
	public void destroy() {
		filterConfig = null;
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest httpRequest = (HttpServletRequest)request;
		HttpServletResponse httpResponse = (HttpServletResponse)response;
		HttpSession session = httpRequest.getSession();
		
		String requestPath = httpRequest.getServletPath();
		String contextPath = httpRequest.getContextPath();
		SecurityUser user = (SecurityUser) session.getAttribute(Constants.USER_KEY);
		//System.out.println("User : "+user);
		//System.out.println("Context :"+contextPath);
		System.out.println("Request :"+requestPath);
		if (user == null && !requestPath.equals(Constants.LOGIN_VIEW)) {
			// redirect to welcome page
			//System.out.println("Redirect");
			session.setAttribute(Constants.ORIGINAL_VIEW_KEY, requestPath);
			httpResponse.sendRedirect(contextPath+Constants.LOGIN_VIEW);
		} else {
			//System.out.println("Render");
			session.removeAttribute(Constants.ORIGINAL_VIEW_KEY);
			chain.doFilter(request, response);
		}
		
	}


}
