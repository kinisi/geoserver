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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.kinisi.geo.data.ApiToken;

public class ApiTokenFilter implements Filter {

  private Logger logger = LoggerFactory.getLogger(ApiTokenFilter.class);
    
  public static final String REMOTE_ADDR_HEADER = "X-Forwarded-For";
  
	public static final String TOKEN_HEADER_NAME = "X-Api-Token";
	public static final String TOKEN_PARAM_NAME = "api_token";
			
	public static final String API_TOKEN_REQUEST_KEY = "cc.kinisi.request.ApiToken";
	
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
	  
	  String balancerIp = request.getRemoteAddr();
		if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
		  HttpServletRequest httpReq = (HttpServletRequest) request;
		  String sourceIp = httpReq.getHeader(REMOTE_ADDR_HEADER);
			String tokenString = getTokenValue(httpReq);
			ApiToken apiToken = controller.getApiToken(tokenString);
			if (apiToken != null && apiToken.isValid()) {
			  logger.debug("Validated API token {} from source IP {} via {}", tokenString, sourceIp, balancerIp);
			  request.setAttribute(API_TOKEN_REQUEST_KEY, apiToken);			  
				chain.doFilter(request, response);
			} else {
			  logger.warn("Rejected API token {} from source IP {} via {}", tokenString, sourceIp, balancerIp);
				((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED);
			}
		} else {
		  logger.error("ServletRequest/ServletResponse not of HTTP type!");
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
