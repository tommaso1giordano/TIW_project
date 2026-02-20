package TIW_project_JS.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import TIW_project_JS.Beans.Docente;

public class ProfessorChecker implements Filter {
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		HttpSession session = req.getSession();
		
		Docente docente = null;
		docente = (Docente) session.getAttribute("docente");
		if (docente == null) {
			res.setStatus(HttpServletResponse.SC_FORBIDDEN);
			res.getWriter().println("you do not have permission");
			return;
		}
		
		chain.doFilter(request, response);
	}

}
