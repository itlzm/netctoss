package web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginFilter implements Filter {

	private String[] paths;
	public void init(FilterConfig filterConfig) throws ServletException {
		String excludePath = filterConfig.getInitParameter("excludePath");
		paths = excludePath.split(",");

	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse res = (HttpServletResponse)response;
		String path = req.getServletPath();
		for(String p:paths){
			if(path.equals(p)){
				chain.doFilter(req, res);
				return;
			}
		}
		HttpSession session = req.getSession();
		String user = (String)session.getAttribute("user");
		if(user==null){
			res.sendRedirect(req.getContextPath()+"/toLogin.do");
		}else{
			chain.doFilter(req, res);
		}
	}

	public void destroy() {
		// TODO Auto-generated method stub

	}

}
