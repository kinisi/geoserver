package cc.kinisi.geo.server;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.cayenne.BaseContext;
import org.apache.cayenne.CayenneRuntimeException;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.query.SelectQuery;

import cc.kinisi.geo.data.ApiToken;
import cc.kinisi.geo.data.DeviceLocation;

@WebListener
public class ServerController implements ServletContextListener {

	public static final String CONTROLLER_NAME = ServerController.class.getName();
	
	public ObjectContext getContext() {
		return BaseContext.getThreadObjectContext();
	}
	
	@SuppressWarnings("unchecked")
	public List<DeviceLocation> getDeviceLocations(String id) {
		SelectQuery sel = new SelectQuery(DeviceLocation.class);
		sel.setQualifier(Expression.fromString("deviceId = '" + id + "'"));
		return getContext().performQuery(sel);
	}
	
	/**
	 * 
	 * @param token
	 * @return {@link ApiToken} matching token, or <code>null</code> if either
	 *         none exists or more than one result is found.
	 */
	@SuppressWarnings("rawtypes")
	public ApiToken getApiToken(String token) {
		SelectQuery q = new SelectQuery(ApiToken.class);
		q.setQualifier(Expression.fromString("token = '" + token + "'"));
		List apiTokens = getContext().performQuery(q);
		return (ApiToken) (apiTokens.size() == 1 ?  apiTokens.get(0) : null);
	}
	
	public boolean isValidTokenValue(String token) {
		if (token != null) {
			ApiToken t = getApiToken(token);
			if (t != null)
				return t.isValid();
		}
		return false;
	}

	public void saveDeviceLocations(List<DeviceLocation> locs)
			throws IOException {

		ObjectContext context = getContext();
		try {
			for (DeviceLocation loc : locs) {
				context.registerNewObject(loc);
			}
			context.commitChanges();
		} catch (CayenneRuntimeException e) {
			context.rollbackChanges();
			String msg = e.getMessage();
			if (e.getCause() != null) {
				msg += ": " + e.getCause().getMessage();
			}
			throw new IOException(msg, e);
		}
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext c = sce.getServletContext();
		c.setAttribute(CONTROLLER_NAME, this);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		ServletContext c = sce.getServletContext();
		c.setAttribute(CONTROLLER_NAME, null);
	}
}
