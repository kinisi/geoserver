package cc.kinisi.geo.server;

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

public class ApiTokenFilter implements Filter {

	public static final String TOKEN_HEADER_NAME = "X-Api-Token";
	public static final String TOKEN_PARAM_NAME = "api_token";
			
	private ServerController controller;

	/**
	 * Searches request header and query parameter (if GET) for API token value.
	 * 
	 * @param req
	 * @return token value present in header if present, else token value 
	 * in query param if this is a GET method
	 */
	private static String getTokenValue(HttpServletRequest req) {
		String token = req.getHeader(TOKEN_HEADER_NAME);
		if (token == null && "GET".equals(req.getMethod())) {
			token = req.getParameter(TOKEN_PARAM_NAME);
		}
		return token;
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
			String token = getTokenValue(((HttpServletRequest) request));
			if (controller.isValidTokenValue(token)) {
				chain.doFilter(request, response);
			} else {
				((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED);
			}
		}

	}
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		ServletContext c = filterConfig.getServletContext();
		controller = (ServerController) c.getAttribute(ServerController.CONTROLLER_NAME);
	}

	@Override
	public void destroy() {
		controller = null;
	}

}
