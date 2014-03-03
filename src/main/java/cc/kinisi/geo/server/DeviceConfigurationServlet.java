package cc.kinisi.geo.server;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cc.kinisi.geo.data.DeviceConfiguration;

@WebServlet("/api/config")
public class DeviceConfigurationServlet extends HttpServlet {

	private ServerController controller;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		ServletContext c = config.getServletContext();
		controller = (ServerController) c.getAttribute(ServerController.CONTROLLER_NAME);
	}

	private static final long serialVersionUID = 1L;

	private static final String PARAM_DEVICE_ID = "device_id";
	
	private static final String ERR_MISSING_DEVICE_ID = "Valid " + PARAM_DEVICE_ID + " parameter must be provided.";
	private static final String ERR_CONFIG_NOT_FOUND = "No config for deviceId";
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
				
		try {
			
			String deviceId = req.getParameter(PARAM_DEVICE_ID);
			if (deviceId == null) 
				throw new IllegalArgumentException(ERR_MISSING_DEVICE_ID);
			
			DeviceConfiguration conf = controller.getDeviceConfiguration(deviceId);
			if (conf != null) 
				resp.getWriter().write(conf.getValue());
			else
				resp.sendError(HttpServletResponse.SC_NOT_FOUND, ERR_CONFIG_NOT_FOUND + " " + deviceId);
			
		} catch (IllegalArgumentException e) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
		} catch (MoreThanOneException e) {
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

}
