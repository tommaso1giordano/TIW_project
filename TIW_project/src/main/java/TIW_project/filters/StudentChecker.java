package TIW_project.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import TIW_project.Beans.Studente;

public class StudentChecker implements Filter {
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		String loginpath = req.getServletContext().getContextPath() + "/index.html";
		HttpSession session = req.getSession();
		
		Studente studente = null;
		studente = (Studente) session.getAttribute("studente");
		if (studente == null) {
			res.sendRedirect(loginpath);
			return;
		}
		
		chain.doFilter(request, response);
	}

}
