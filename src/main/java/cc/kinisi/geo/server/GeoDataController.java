package cc.kinisi.geo.server;

import java.io.IOException;
import java.util.List;

import org.apache.cayenne.BaseContext;
import org.apache.cayenne.CayenneRuntimeException;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.query.SelectQuery;

import cc.kinisi.geo.data.ApiToken;
import cc.kinisi.geo.data.DeviceLocation;

public class GeoDataController {

	@SuppressWarnings("unchecked")
	public static List<DeviceLocation> getDeviceLocations(String id) {
		ObjectContext context = BaseContext.getThreadObjectContext();
		SelectQuery sel = new SelectQuery(DeviceLocation.class);
		sel.setQualifier(Expression.fromString("deviceId = '" + id + "'"));
		return context.performQuery(sel);
	}
	
	/**
	 * 
	 * @param token
	 * @return {@link ApiToken} matching token, or <code>null</code> if either
	 *         none exists or more than one result is found.
	 */
	public static ApiToken getApiToken(String token) {
		ObjectContext context = BaseContext.getThreadObjectContext();
		SelectQuery q = new SelectQuery(ApiToken.class);
		q.setQualifier(Expression.fromString("token = '" + token + "'"));
		@SuppressWarnings("rawtypes") List apiTokens = context.performQuery(q);
		return (ApiToken) (apiTokens.size() == 1 ?  apiTokens.get(0) : null);
	}

	public static void saveDeviceLocations(List<DeviceLocation> locs)
			throws IOException {

		ObjectContext context = BaseContext.getThreadObjectContext();
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
}
