package cc.kinisi.geo.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cc.kinisi.geo.data.DeviceConfiguration;

@WebServlet("/api/config")
public class DeviceConfigurationServlet extends KinisiServlet {

	private static final long serialVersionUID = 1L;

	private static final String ERR_CONFIG_NOT_FOUND = "No config for deviceId";
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
				
		try {
		  			
			String deviceId = getAuthorizedDeviceId(req);
			DeviceConfiguration conf = getController().getDeviceConfiguration(deviceId);
			if (conf != null) 
				resp.getWriter().write(conf.getValue());
			else
				resp.sendError(HttpServletResponse.SC_NOT_FOUND, ERR_CONFIG_NOT_FOUND + " " + deviceId);
			
		} catch (IllegalArgumentException e) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
		} catch (UnauthorizedException e) {
      resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
		} catch (MoreThanOneException e) {
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

}
