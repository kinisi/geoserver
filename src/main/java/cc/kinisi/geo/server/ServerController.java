package cc.kinisi.geo.server;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;

import org.apache.cayenne.BaseContext;
import org.apache.cayenne.CayenneRuntimeException;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.query.SQLTemplate;
import org.apache.cayenne.query.SelectQuery;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import cc.kinisi.geo.data.ApiToken;
import cc.kinisi.geo.data.DeviceConfiguration;
import cc.kinisi.geo.data.DeviceInterface;
import cc.kinisi.geo.data.DeviceLocation;

@WebListener
public class ServerController implements ServletContextListener {

	public static final String CONTROLLER_NAME = ServerController.class.getName();
		
	private static final String TOKEN_AUTH_FORMAT = "Token not authorized for device ID: %s";
	
  private static final String DLOC_UPSERT_SQL = "insert into device_location "
      + "(device_id, latitude, longitude, altitude, climb, measure_time, receive_time, speed, track, epx, epy, epv) "
      + "values ('%s',%.9f,%.9f,%.9f,%.9f,'%s','%s',%.9f,%.9f,%.9f,%.9f,%.9f) on duplicate key update "
      + "altitude = values(altitude), "
      + "climb = values(climb), "
      + "measure_time = values(measure_time), "
      + "receive_time = values(receive_time), "
      + "speed = values(speed), "
      + "track = values(track), "
      + "epx = values(epx), "
      + "epy = values(epy), "
      + "epv = values(epv)";

	public ObjectContext getContext() {
		return BaseContext.getThreadObjectContext();
	}
	
	@SuppressWarnings("unchecked")
	public List<DeviceLocation> getDeviceLocations(String id, DateTime startTime, DateTime endTime) {
		if (startTime == null)
		  startTime = new DateTime(0);
		if (endTime == null)
		  endTime = DateTime.now();
	  DateTimeFormatter f = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		String s = f.print(startTime);
		String e = f.print(endTime);
		String exp = String.format("deviceId = '%s' and measureTime >= '%s' and measureTime <= '%s'", id, s, e);
    SelectQuery sel = new SelectQuery(DeviceLocation.class);
		sel.setQualifier(Expression.fromString(exp));
		return getContext().performQuery(sel);
	}
	
	@SuppressWarnings("unchecked")
  public DeviceConfiguration getDeviceConfiguration(String id) {
		SelectQuery sel = new SelectQuery(DeviceConfiguration.class);
		sel.setQualifier(Expression.fromString("deviceId = '" + id + "'"));
		List<DeviceConfiguration> confs = getContext().performQuery(sel);
		int n = confs.size();
		if (n > 1)
			throw new MoreThanOneException("Multiple DeviceConfigurations found for deviceId " + id);
		return n == 1 ? confs.get(0) : null;
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
			throws CayenneRuntimeException {
	  
    ObjectContext context = getContext();    
    try {
      for (DeviceLocation l : locs) {
        context.registerNewObject(l);
        String devid = l.getDeviceId();
        Double lat = l.getLatitude();
        Double lon = l.getLongitude();
        Double alt = l.getAltitude();
        Double climb = l.getClimb();
        String mtime = l.getFormattedMeasureTime("yyyy-MM-dd HH:mm:ss");
        String rtime = l.getFormattedReceiveTime("yyyy-MM-dd HH:mm:ss");
        Double speed = l.getSpeed();
        Double track = l.getTrack();
        Double epx = l.getEpx();
        Double epy = l.getEpy();
        Double epv = l.getEpv();
        String sql = String.format(DLOC_UPSERT_SQL, devid, lat, lon, alt, climb, mtime, rtime, speed, track, epx, epy, epv);
        SQLTemplate upsert = new SQLTemplate(DeviceLocation.class, sql);
        context.performGenericQuery(upsert);
      }
    } finally {
      context.rollbackChanges();
    }

	}
	
	public void createNewDeviceConfigurationWithIdForToken(String id, ApiToken token) {
	  ObjectContext context = getContext();
	  DeviceConfiguration config = context.newObject(DeviceConfiguration.class);
    config.setDeviceId(id);
    config.setApiToken(token);
    config.setValue("{}");
    context.commitChanges();
	}

  public List<String> getAuthorizedDeivceIds(ApiToken t) {
    List<DeviceConfiguration> configs = t.getDeviceConfigurations();
    List<String> deviceIds = new ArrayList<>(configs.size());
    for (DeviceConfiguration c : configs) {
      String id = c.getDeviceId();
      if (id != null)
        deviceIds.add(id);
    }
    return deviceIds;
  }
  
  public void authorizeDeviceIdForRequest(String id, HttpServletRequest req) throws UnauthorizedException {
    ApiToken token = (ApiToken) req.getAttribute(ApiTokenFilter.API_TOKEN_REQUEST_KEY);
    List<String> authorizedIds = getAuthorizedDeivceIds(token);
    if (!authorizedIds.contains(id)) {
      DeviceConfiguration config = getDeviceConfiguration(id);
      if (config == null) {
        createNewDeviceConfigurationWithIdForToken(id, token);
        authorizedIds = getAuthorizedDeivceIds(token);
      } else {
        throw new UnauthorizedException(String.format(TOKEN_AUTH_FORMAT, id));
      }
    }
  }
  
  public void updateDeviceInterfacesForDeviceId(List<DeviceInterface> nics, String deviceId) {
    DeviceConfiguration conf = getDeviceConfiguration(deviceId);
    List<DeviceInterface> oldInterfaces = conf.getDeviceInterfaces();
    ObjectContext c = getContext();
    c.deleteObjects(oldInterfaces);
    c.commitChanges();
    for(DeviceInterface i : nics) {
      conf.addToDeviceInterfaces(i);
    }
    c.commitChanges();
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
