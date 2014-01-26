package cc.kinisi.geo.server;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ApiTokenFilter implements Filter {

	public static final String TOKEN_HEADER_NAME = "X-Api-Token";
			
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
			String token = ((HttpServletRequest) request).getHeader(TOKEN_HEADER_NAME);
			if (GeoDataController.getApiToken(token).isValid()) {
				chain.doFilter(request, response);
			} else {
				((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED);
			}
		}
		
		
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}

}
